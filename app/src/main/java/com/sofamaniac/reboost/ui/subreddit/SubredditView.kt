/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.subreddit

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sofamaniac.reboost.BottomBar
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditName
import com.sofamaniac.reboost.domain.repository.feed.HomeRepository
import com.sofamaniac.reboost.domain.repository.feed.SubredditPostsRepository
import com.sofamaniac.reboost.ui.TabBar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditViewer(
    subreddit: SubredditName,
    navController: NavController,
    selected: State<Int>,
    modifier: Modifier = Modifier,
    viewModel: SubredditViewModel = hiltViewModel<SubredditViewModel, SubredditViewModel.Factory> {
        factory -> factory.create(subreddit.name)
    },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            TopBar(
                subreddit.name,
                viewModel,
                rememberDrawerState(DrawerValue.Closed), scrollBehavior,
            )
        },
        bottomBar = { TabBar(selected) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PostFeedViewer(
            viewModel,
            navController,
            showSubredditIcon = false,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@HiltViewModel(assistedFactory = SubredditViewModel.Factory::class)
class SubredditViewModel @AssistedInject constructor(
    repository: SubredditPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    @Assisted private val subredditName: String
): PostFeedViewModel(repository, visitedPostsDao) {

    init {
        repository.updateSubreddit(SubredditName(subredditName))
    }

    @AssistedFactory
    interface Factory {
        fun create(subreddit: String): SubredditViewModel
    }

}


