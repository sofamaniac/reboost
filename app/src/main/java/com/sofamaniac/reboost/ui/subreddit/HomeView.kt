/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.subreddit

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.domain.repository.feed.HomeRepository
import com.sofamaniac.reboost.ui.TabBar
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewer(
    navController: NavController,
    drawerState: DrawerState,
    selected: State<Int>,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { TopBar("Home", viewModel, drawerState, scrollBehavior) },
        bottomBar = { TabBar(selected) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PostFeedViewer(
            viewModel,
            navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: HomeRepository,
    visitedPostsDao: VisitedPostsDao
): PostFeedViewModel(repository, visitedPostsDao)


