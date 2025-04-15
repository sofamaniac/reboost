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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sofamaniac.reboost.BottomBar
import com.sofamaniac.reboost.reddit.subreddit.HomeRepository


class HomeView : PostFeedViewModel(title = "Home", repository = HomeRepository())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewer(
    navController: NavController,
    selected: MutableIntState,
    viewModel: HomeView = viewModel(factory = HomeViewModelFactory()),
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = { viewModel.TopBar(rememberDrawerState(DrawerValue.Closed), scrollBehavior) },
        bottomBar = { BottomBar(navController, selected) },
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

class HomeViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeView::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeView() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
