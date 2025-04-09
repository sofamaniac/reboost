package com.sofamaniac.reboost

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.sofamaniac.reboost.auth.BasicAuthClient
import com.sofamaniac.reboost.auth.Manager
import com.sofamaniac.reboost.auth.StoreManager
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.ui.subreddit.HomeView
import com.sofamaniac.reboost.ui.subreddit.HomeViewer
import com.sofamaniac.reboost.ui.subreddit.SubredditViewer
import com.sofamaniac.reboost.ui.subreddit.PostFeedViewer
import com.sofamaniac.reboost.ui.subreddit.SavedView
import com.sofamaniac.reboost.ui.subreddit.SavedViewer
import com.sofamaniac.reboost.ui.subreddit.SubredditView
import com.sofamaniac.reboost.ui.subredditList.SubredditListViewer
import com.sofamaniac.reboost.ui.theme.ReboostTheme
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import java.util.concurrent.Flow.Subscription


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
            Log.d("MainActivity", "Token request: ${tokenExchangeRequest.jsonSerializeString()}")
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
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                authState.authManager,
                onClick = {
                    onLoginClicked()
                    scope.launch { drawerState.close() }
                },
            )
        },
    ) {
        NavigationGraph(
            navController,
        )
    }
}

@Composable
fun LoginButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = { onClick() }, modifier = modifier) {
        Text(
            "Login",
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun DrawerContent(
    authManager: StoreManager,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            if (!authManager.loggedIn) {
                Spacer(modifier = Modifier.padding(16.dp))
                LoginButton(
                    modifier = modifier.padding(16.dp),
                    onClick = onClick
                )
            } else {
                var username by remember { mutableStateOf("Anonymous") }
                if (username == "Anonymous") {
                    LaunchedEffect(username) {
                        username =
                            RedditAPI.service.getIdentity().body()?.username
                                ?: "Anonymous"
                    }
                }
                Text(
                    "Hello $username!",
                    modifier = modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
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
            HomeViewer(navController, selected)
        }
        composable<Subscriptions> {
            selected.intValue = 2
            SubredditListViewer(navController = navController)
        }
        composable<Search> {
            selected.intValue = 1
            PostFeedViewer(HomeView(), navController, selected)
        }
        composable<Inbox> {
            SubredditViewer(
                navController, selected,
                viewModel {
                    SubredditView(
                        "arknuts"
                    )
                }
            )
        }
        composable<Subreddit> { navBackStackEntry ->
            selected.intValue = 2
            val subreddit = navBackStackEntry.toRoute<Subreddit>().subreddit
            SubredditViewer(
                navController, selected,
                viewModel {
                    SubredditView(
                        subreddit
                    )
                }
            )
        }
        composable<Profile> { navBackStackEntry ->
            selected.intValue = 4
            SavedViewer(navController, selected)
        }
//        composable<Post> { navBackStackEntry ->
//            val post = navBackStackEntry.toRoute<Post>().post
//        }
    }
}