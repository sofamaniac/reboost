/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:14 PM
 *
 */

package com.sofamaniac.reboost

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditName
import com.sofamaniac.reboost.ui.ProfileView
import com.sofamaniac.reboost.ui.drawer.DrawerContent
import com.sofamaniac.reboost.ui.drawer.DrawerViewModel
import com.sofamaniac.reboost.ui.subreddit.HomeViewer
import com.sofamaniac.reboost.ui.subreddit.SubredditViewer
import com.sofamaniac.reboost.ui.subredditList.SubredditListViewer
import com.sofamaniac.reboost.ui.theme.ReboostTheme
import com.sofamaniac.reboost.ui.thread.ThreadView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReboostTheme {
                MaterialTheme {
                    val navController = rememberNavController()
                    // Setup nav controller
                    CompositionLocalProvider(LocalNavController provides navController) {
                        MainScreen(
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerViewModel: DrawerViewModel = viewModel()
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                viewModel = drawerViewModel,
                onAccountChange = {
                    navController.navigate(Home) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                    scope.launch {drawerState.close()}
                }
            )
        },
    ) {
        NavigationGraph(
            navController,
            drawerState,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {
    //val selected = remember { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selected = remember(navBackStackEntry) {
        val currentRoute = navBackStackEntry?.destination?.route
        derivedStateOf {
            when  {
                currentRoute?.contains(Home::class.qualifiedName ?: "") == true-> 0
                currentRoute?.contains(SearchRoute::class.qualifiedName ?: "") == true -> 1
                currentRoute?.contains(SubredditRoute::class.qualifiedName ?: "") == true -> 2
                currentRoute?.contains(SubscriptionsRoute::class.qualifiedName ?: "") == true -> 2
                currentRoute?.contains(InboxRoute::class.qualifiedName ?: "") == true ->3
                currentRoute?.contains(ProfileRoute::class.qualifiedName ?: "") == true -> 4
                else -> {
                    Log.w("NavigationGraph", "Unknown route: $currentRoute")
                    0
                }
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier.fillMaxSize()
    ) {
        composable<Home> {
            HomeViewer(
                navController, drawerState, selected
            )
        }
        composable<PostRoute> {
            ThreadView(selected)
        }
        composable<SubscriptionsRoute> {
            SubredditListViewer(navController = navController)
        }
        composable<SearchRoute> {
            SubredditViewer(
                SubredditName("artknights"),
                navController, selected,
            )
        }
        composable<InboxRoute> {
            SubredditViewer(
                SubredditName("artknights"),
                navController, selected,
            )
        }
        composable<SubredditRoute> { navBackStackEntry ->
            val subreddit = navBackStackEntry.toRoute<SubredditRoute>().subreddit
            val subredditName = SubredditName(subreddit)
            SubredditViewer(
                subredditName,
                navController,
                selected,
            )
        }
        composable<ProfileRoute> { navBackStackEntry ->
            val user = navBackStackEntry.toRoute<ProfileRoute>().author
            ProfileView(user, selected = selected, drawerState = rememberDrawerState(DrawerValue.Closed))
        }
        composable<LicensesRoute> {
            LicenseWebView()
        }
    }
}