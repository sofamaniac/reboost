package com.sofamaniac.reboost.ui.subreddit

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sofamaniac.reboost.BottomBar
import com.sofamaniac.reboost.reddit.subreddit.SavedRepository
import kotlinx.coroutines.launch

class SavedView : PostFeedViewModel(title = "Saved Posts", repository = SavedRepository()) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun TopBar(drawerState: DrawerState, scrollBehavior: TopAppBarScrollBehavior?) {
        val scope = rememberCoroutineScope()
        TopAppBar(
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text("Saved Posts")
            },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch { drawerState.open() }
                }) { Icon(Icons.Default.Menu, "") }
            },
            actions = {
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedViewer(
    navController: NavController,
    selected: MutableIntState,
    viewModel: SavedView = viewModel(),
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
