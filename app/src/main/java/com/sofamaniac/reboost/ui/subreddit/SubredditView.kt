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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import com.sofamaniac.reboost.BottomBar
import com.sofamaniac.reboost.reddit.subreddit.SubredditName
import com.sofamaniac.reboost.reddit.subreddit.SubredditPostsRepository

class SubredditView(val subreddit: SubredditName) : PostFeedViewModel(
    title = subreddit.name, repository = SubredditPostsRepository(
        subreddit
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditViewer(
    navController: NavController,
    selected: MutableIntState,
    viewModel: SubredditView,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = { viewModel.TopBar(rememberDrawerState(DrawerValue.Closed), scrollBehavior) },
        bottomBar = { BottomBar(selected) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PostFeedViewer(
            viewModel,
            navController,
            selected,
            showSubredditIcon = false,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
