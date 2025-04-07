package com.sofamaniac.reboost.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.reboost.Tab
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.Sort
import com.sofamaniac.reboost.reddit.Timeframe
import com.sofamaniac.reboost.ui.post.PostViewModel
import com.sofamaniac.reboost.ui.post.SubredditPostsRepository
import com.sofamaniac.reboost.ui.post.View
import kotlinx.coroutines.launch

/**
 * Data class representing the current state of a SubredditViewer
 */
data class SubredditViewerState(
    val subreddit: String,
    val initialSort: Sort = Sort.Hot,
    val initialTimeframe: Timeframe? = null,
    val listState: LazyListState = LazyListState()
) : Tab {
    var sort by mutableStateOf(initialSort)
        private set
    var timeframe by mutableStateOf(initialTimeframe)
        private set
    val title: String get() = subreddit
    var viewModel: PostViewModel? = null

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        this.sort = sort
        this.timeframe = timeframe
        viewModel?.updateSort(sort, timeframe)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun TopBar(
        drawerState: DrawerState,
        scrollBehavior: TopAppBarScrollBehavior?
    ) {
        TopBar(this, drawerState, scrollBehavior)
    }

    @Composable
    override fun Content(modifier: Modifier) {
        SubredditViewer(this, modifier)
    }
}

/**
 * Composable function to display a list of posts from a subreddit.
 *
 * @param state The current state of the SubredditViewer, including subreddit, sort order, and timeframe.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditViewer(state: SubredditViewerState, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = remember(state.viewModel) {
        // Initialize view model
        state.viewModel ?: run {
            val repository = SubredditPostsRepository(
                subreddit = state.subreddit,
                apiService = RedditAPI.getApiService(context),
                sort = state.sort,
                timeframe = state.timeframe
            )
            PostViewModel(repository)
        }.also {
            state.viewModel = it
        }
    }

    LaunchedEffect(state.sort, state.timeframe) {
        state.updateSort(state.sort, state.timeframe)
        viewModel.refresh()
    }
    val posts = viewModel.data.collectAsLazyPagingItems()
    //val listState = state.viewModel!!.rememberLazyListState()
    val listState = remember { state.listState }
    PullToRefreshBox(
        isRefreshing = posts.loadState.refresh == LoadState.Loading,
        onRefresh = {
            posts.refresh()
        },
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState,
        ) {
            items(count = posts.itemCount, key = posts.itemKey { it.id() }) { index ->
                posts[index]?.let { post ->
                    View(post)
                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    state: SubredditViewerState,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior?
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Column {
                Text(state.title)
                Row(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(state.sort.toString(), style = MaterialTheme.typography.labelSmall)
                    if (state.timeframe != null) {
                        Text(".")
                        Text(
                            state.timeframe.toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch { drawerState.open() }
            }) { Icon(Icons.Default.Menu, "") }
        },
        actions = {
            SortMenu(state)
        }
    )
}

@Composable
fun SortMenu(state: SubredditViewerState) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Best") }, onClick = {
                state.updateSort(Sort.Best)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Hot") }, onClick = {
                state.updateSort(Sort.Hot)
                expanded = false
            })
            DropdownMenuItem(text = { Text("New") }, onClick = {
                state.updateSort(Sort.New)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Top") }, onClick = {
                //TODO: add top menu
            })
            DropdownMenuItem(text = { Text("Controversial") }, onClick = {})
            DropdownMenuItem(text = { Text("Rising") }, onClick = {})
        }
    }
}
