/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.subreddit

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.sofamaniac.reboost.AccountsViewModel
import com.sofamaniac.reboost.BottomBar
import com.sofamaniac.reboost.accounts.RedditAccount


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewer(
    navController: NavController,
    drawerState: DrawerState,
    selected: MutableIntState,
    accountsViewModel: AccountsViewModel,
    viewModel: PostFeedViewModel,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val account by accountsViewModel.activeAccount.collectAsState(initial = RedditAccount.anonymous())
    val posts = viewModel.data.collectAsLazyPagingItems()
    LaunchedEffect(account) {
        Log.d("HomeViewer", "account: $account")
        posts.refresh()
    }
    Scaffold(
        topBar = { TopBar("Home", viewModel, drawerState, scrollBehavior) },
        bottomBar = { BottomBar(selected) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PostFeedViewer(
            viewModel,
            navController,
            selected,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
//
//class HomeViewModelFactory() : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(HomeView::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return HomeView() as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
