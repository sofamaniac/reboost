package com.sofamaniac.reboost.ui.subredditList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sofamaniac.reboost.Routes
import com.sofamaniac.reboost.Tab
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.ui.subreddit.SubredditView


class SubscriptionState : Tab {
    var viewModel: SubscriptionViewModel? = null
    var currentSearch by mutableStateOf("")

    @Composable
    override fun Content(modifier: Modifier) {
        SubredditListViewer(this)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun TopBar(drawerState: DrawerState, scrollBehavior: TopAppBarScrollBehavior?) {
        var expanded by remember { mutableStateOf(true) }
        TopAppBar(scrollBehavior = scrollBehavior, title = {
            Row {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back to Home")
                }
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = currentSearch,
                            onQueryChange = { currentSearch = it },
                            onSearch = { },
                            placeholder = { Text("Go to ...") },
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
//                        modifier = TODO(),
//                        enabled = TODO(),
//                        leadingIcon = TODO(),
//                        trailingIcon = TODO(),
//                        colors = TODO(),
//                        interactionSource = TODO(),
                        )
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
//                modifier = TODO(),
//                shape = TODO(),
//                colors = TODO(),
//                tonalElevation = TODO(),
//                shadowElevation = TODO(),
//                windowInsets = TODO(),
                ) { }
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditListViewer(state: SubscriptionState) {
    val repository = remember(state) {
        Repository(
            apiService = RedditAPI.service
        )
    }

    state.viewModel = state.viewModel ?: SubscriptionViewModel(repository)

    val subs = state.viewModel!!.data.collectAsLazyPagingItems()
    val lazyListState = state.viewModel!!.rememberLazyListState()
    val sortedSubs = subs.itemSnapshotList.items.sortedBy { it.data.display_name }

    PullToRefreshBox(
        isRefreshing = subs.loadState.refresh == LoadState.Loading,
        onRefresh = {
            subs.refresh()
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState,
        ) {
            items(count = sortedSubs.size) { index ->
                sortedSubs[index].let { subs ->
                    Text(
                        text = subs.data.display_name,
                        modifier = Modifier.clickable {
                            Routes.subscriptions.state = SubredditView(subs.data.display_name)
                        })
                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
