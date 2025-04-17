/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.subreddit

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
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.reboost.Tab
import com.sofamaniac.reboost.reddit.post.PostRepository
import com.sofamaniac.reboost.reddit.post.PostsSource
import com.sofamaniac.reboost.reddit.post.Sort
import com.sofamaniac.reboost.reddit.post.Timeframe
import com.sofamaniac.reboost.ui.post.PostBody
import com.sofamaniac.reboost.ui.post.View
import kotlinx.coroutines.launch

/**
 * Data class representing the current state of a SubredditViewer
 */
abstract class PostFeedViewModel(
    val title: String = "",
    val repository: PostRepository,
    initialSort: Sort = Sort.Best,
    initialTimeframe: Timeframe? = null,
) : Tab, ViewModel() {
    var listState by mutableStateOf(LazyListState())
    var sort by mutableStateOf(initialSort)
    var timeframe by mutableStateOf(initialTimeframe)

    // We keep a reference to the PostsSource in order to invalidate it when changing sort
    private var postsSource: PostsSource? = null
    var data = Pager(
        config = PagingConfig(pageSize = 100),
        initialKey = "",
        pagingSourceFactory = {
            PostsSource(repository).also {
                postsSource = it
            }
        }
    ).flow.cachedIn(viewModelScope)


    /**
     * Updates the current sorting criteria and optionally the timeframe.
     *
     * This function updates the internal `sort` and `timeframe` properties of the current object.
     * It also triggers an update in the associated ViewModel if one is present.
     *
     * If the provided `sort` and `timeframe` are the same as the current values, the function
     * will return early without performing any updates. This prevents unnecessary refresh operations.
     *
     * @param sort The new sorting criteria to apply.
     * @param timeframe The new timeframe to apply, or null to clear the timeframe. Defaults to null.
     *
     * @see Sort
     * @see com.sofamaniac.reboost.reddit.post.Timeframe
     */
    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        if (this.sort == sort && this.timeframe == timeframe) return
        this.sort = sort
        this.timeframe = timeframe
        repository.updateSort(sort, timeframe)
        postsSource?.invalidate()
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
    override fun Content(
        navController: NavController,
        selected: MutableIntState,
        modifier: Modifier
    ) {
        PostFeedViewer(this, navController, selected, modifier)
    }
}

/**
 * Composable function to display a list of posts from a subreddit.
 *
 * @param state The current state of the SubredditViewer, including subreddit, sort order, and timeframe.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFeedViewer(
    state: PostFeedViewModel,
    navController: NavController,
    selected: MutableIntState,
    modifier: Modifier = Modifier,
    showSubredditIcon: Boolean = true
) {

    LaunchedEffect(state.sort, state.timeframe) {
        state.updateSort(state.sort, state.timeframe)
    }
    val posts = state.data.collectAsLazyPagingItems()
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
            items(count = posts.itemCount, key = posts.itemKey { it.data.id.id }) { index ->
                posts[index]?.let { post ->
                    View(post, selected, showSubredditIcon = showSubredditIcon) {
                        PostBody(post)
                    }
                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    state: PostFeedViewModel,
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
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(state.sort.toString(), style = MaterialTheme.typography.labelSmall)
                    if (state.timeframe != null) {
                        Text(".", style = MaterialTheme.typography.labelSmall)
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
            }) { Icon(Icons.Default.Menu, "Open Drawer") }
        },
        actions = {
            // Sort Dropdown
            var showMenu by remember { mutableStateOf(false) }
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Filled.MoreVert, "Options")
            }

            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(onClick = { }, text = { Text("Settings") })
                DropdownMenuItem(onClick = { }, text = { Text("Info") })
            }
            SortMenu(state)
        }
    )
}

@Composable
fun SortMenu(state: PostFeedViewModel) {
    var sortExpanded = remember { mutableStateOf(false) }
    var timeframeExpanded = remember { mutableStateOf(false) }
    var chosenSort by remember { mutableStateOf(state.sort) }
    Box {
        IconButton(onClick = { sortExpanded.value = true }) {
            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
        }
        DropdownMenu(
            expanded = sortExpanded.value,
            onDismissRequest = { sortExpanded.value = false }) {
            Sort.entries.forEach { sort ->
                if (sort.isTimeframe()) {
                    DropdownMenuItem(text = { Text(sort.toString()) }, onClick = {
                        timeframeExpanded.value = true
                        chosenSort = sort
                    })
                } else {
                    DropdownMenuItem(text = { Text(sort.toString()) }, onClick = {
                        state.updateSort(sort)
                        sortExpanded.value = false
                    })
                }
            }
        }
        TimeframeMenu(state, sortExpanded, timeframeExpanded, chosenSort)
    }
}

@Composable
fun TimeframeMenu(
    state: PostFeedViewModel,
    sortExpanded: MutableState<Boolean>,
    expanded: MutableState<Boolean>,
    sort: Sort
) {
    DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
        Timeframe.entries.forEach {
            DropdownMenuItem(text = { Text(it.toString()) }, onClick = {
                state.updateSort(sort, it)
                expanded.value = false
                sortExpanded.value = false
            })
        }
    }
}