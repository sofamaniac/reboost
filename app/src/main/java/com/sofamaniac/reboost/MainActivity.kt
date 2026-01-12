/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:14 PM
 *
 */

package com.sofamaniac.reboost

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sofamaniac.reboost.auth.BasicAuthClient
import com.sofamaniac.reboost.auth.Manager
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.subreddit.SubredditDao
import com.sofamaniac.reboost.reddit.subreddit.SubredditEntity
import com.sofamaniac.reboost.reddit.subreddit.SubredditName
import com.sofamaniac.reboost.reddit.subreddit.SubredditPostsRepository
import com.sofamaniac.reboost.ui.ProfileView
import com.sofamaniac.reboost.ui.drawer.DrawerContent
import com.sofamaniac.reboost.ui.drawer.DrawerViewModel
import com.sofamaniac.reboost.ui.post.CommentsViewer
import com.sofamaniac.reboost.ui.subreddit.HomeViewModelFactory
import com.sofamaniac.reboost.ui.subreddit.HomeViewer
import com.sofamaniac.reboost.ui.subreddit.PostFeedViewModel
import com.sofamaniac.reboost.ui.subreddit.SubredditViewer
import com.sofamaniac.reboost.ui.subredditList.SubredditListViewer
import com.sofamaniac.reboost.ui.theme.ReboostTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse


@HiltAndroidApp
class ReboostApp : Application()

interface Tab {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(drawerState: DrawerState, scrollBehavior: TopAppBarScrollBehavior?)

    @Composable
    fun Content(
        navController: NavController,
        selected: MutableIntState,
        modifier: Modifier = Modifier
    )
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var authState: Manager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authState = Manager(this)
        RedditAPI.init(this)
        enableEdgeToEdge()
        setContent {
            ReboostTheme {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) {
                    if (it.resultCode == RESULT_OK) {
                        val resp = AuthorizationResponse.fromIntent(it.data!!)
                        val error = AuthorizationException.fromIntent(it.data)
                        authState.authManager.update(resp, error)

                        handleAuthorizationResponse(resp, error) { }
                        // Handle the authorization response
                    } else {
                        Log.e("MainActivity", "onCreate: $it")
                    }
                }
                MaterialTheme {
                    val navController = rememberNavController()
                    // Setup nav controller
                    CompositionLocalProvider(LocalNavController provides navController) {
                        MainScreen(
                            authState,
                            navController = navController,
                            onLoginClicked = {
                                // Create and launch the auth intent here
                                val authIntent =
                                    authState.authService.getAuthorizationRequestIntent(authState.authRequest)
                                launcher.launch(authIntent)
                            },
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authState.dispose()
    }

    private fun handleAuthorizationResponse(
        resp: AuthorizationResponse?,
        error: AuthorizationException?,
        onLoginSuccess: () -> Unit
    ) {
        if (resp != null) {
            val basicAuth = BasicAuthClient(authState.authConfig.clientId)
            val tokenExchangeRequest = resp.createTokenExchangeRequest()
            Log.d(
                "MainActivity",
                "Token request: ${tokenExchangeRequest.jsonSerializeString()}"
            )
            Log.d("MainActivity", "redirect uri: ${authState.authConfig.redirectUri}")
            authState.authService.performTokenRequest(
                tokenExchangeRequest,
                basicAuth
            ) { tokenResponse, tokenException ->
                if (tokenResponse != null) {
                    authState.authManager.update(tokenResponse, tokenException)
                    Log.d("MainActivity", "onCreate: ${authState.authManager.loggedIn}")
                    onLoginSuccess()
                } else {
                    Log.d("MainActivity", "token exception: ${tokenException.toString()}")
                }
            }
        } else {
            Log.e("MainActivity", "auth exception: ${error.toString()}")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authState: Manager,
    navController: NavHostController,
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val accountsViewModel: AccountsViewModel = viewModel()
    val drawerViewModel: DrawerViewModel = viewModel()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                viewModel = drawerViewModel,
            )
        },
    ) {
        NavigationGraph(
            navController,
            drawerState,
            accountsViewModel,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    drawerState: DrawerState,
    accountsViewModel: AccountsViewModel,
    modifier: Modifier = Modifier
) {
    val selected = remember { mutableIntStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier.fillMaxSize()
    ) {
        composable<Home> {
            selected.intValue = 0
            val viewModel: PostFeedViewModel = viewModel(factory = HomeViewModelFactory())
            HomeViewer(
                navController, drawerState, selected, accountsViewModel, viewModel
            )
        }
        composable<SubscriptionsRoute> {
            selected.intValue = 2
            SubredditListViewer(navController = navController)
        }
        composable<SearchRoute> {
            selected.intValue = 1
            SubredditViewer(
                SubredditName("artknights"),
                navController, selected,
                viewModel {
                    PostFeedViewModel(
                        SubredditPostsRepository(
                            SubredditName("artknights")
                        )
                    )
                }
            )
        }
        composable<InboxRoute> {
            selected.intValue = 3
            SubredditViewer(
                SubredditName("artknights"),
                navController, selected,
                viewModel {
                    PostFeedViewModel(
                        SubredditPostsRepository(
                            SubredditName("artknights")
                        )
                    )
                }
            )
        }
        composable<SubredditRoute> { navBackStackEntry ->
            selected.intValue = 2
            val subreddit = navBackStackEntry.toRoute<SubredditRoute>().subreddit
            val subredditName = SubredditName(subreddit)
            SubredditViewer(
                subredditName,
                navController,
                selected,
                viewModel {
                    PostFeedViewModel(
                        SubredditPostsRepository(
                            subredditName
                        )
                    )
                }
            )
        }
        composable<ProfileRoute> { navBackStackEntry ->
            selected.intValue = 4
            val user = navBackStackEntry.toRoute<ProfileRoute>().author
            ProfileView(user, drawerState = rememberDrawerState(DrawerValue.Closed))
        }
        composable<PostRoute> { navBackStackEntry ->
            val post_permalink = navBackStackEntry.toRoute<PostRoute>().post_permalink
            Log.d("NavigationGraph", "post_permalink: $post_permalink")
            var post by remember { mutableStateOf<com.sofamaniac.reboost.reddit.Post?>(null) }
            LaunchedEffect(post_permalink) {
                val temp =
                    RedditAPI.service.getPostFromPermalink(post_permalink).body()?.post
                        ?.first()
                post = temp
            }
            if (post != null) {
                CommentsViewer(selected, post!!)
            }
        }
        composable<LicensesRoute> {
            LicenseWebView()
        }
    }
}

@Database(entities = [SubredditEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subredditDao(): SubredditDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "subreddit_database"
                )
                    // FIXME Destroy all tables on migration
                    .fallbackToDestructiveMigration(true)
                    .build().also { INSTANCE = it }
            }
        }
    }
}