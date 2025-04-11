package com.sofamaniac.reboost.ui.subredditList

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sofamaniac.reboost.Tab
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.Subreddit
import com.sofamaniac.reboost.ui.subreddit.SubredditIcon
import kotlinx.coroutines.launch
import kotlin.collections.plus


class SubscriptionState : Tab, ViewModel() {
    var currentSearch by mutableStateOf("")
    var subreddits by mutableStateOf(listOf<Subreddit>())
    var lazyListState by mutableStateOf(androidx.compose.foundation.lazy.LazyListState())
    var after: String? by mutableStateOf("")

    suspend fun load() {
        val apiService = RedditAPI.service
        while (after != null) {
            val response = apiService.getSubreddits(after)
            val subs: List<Subreddit>? = response.body()?.data?.children
            subreddits = subreddits + (subs ?: emptyList())
            after = response.body()?.data?.after
            Log.d("SubredditState", "After: $after")
        }

    }

    suspend fun refresh() {
        after = ""
        load()
        Log.d("SubredditState", "Subreddits: $subreddits")
    }

    @Composable
    override fun Content(
        navController: NavController,
        selected: MutableIntState,
        modifier: Modifier
    ) {
        SubredditListViewer(this, navController)
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
fun SubredditListViewer(state: SubscriptionState = viewModel(), navController: NavController) {
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(state, isRefreshing) {
        isRefreshing = true
        state.load()
        isRefreshing = false
        // TODO Room with subreddits info (icon, ...)
    }


    val sortedSubs = state.subreddits.sortedBy { it.data.display_name.name.lowercase() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            state.TopBar(
                drawerState = rememberDrawerState(DrawerValue.Closed),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                scope.launch { state.refresh() }
                isRefreshing = false
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = state.lazyListState,
            ) {
                items(count = sortedSubs.size) { index ->
                    sortedSubs[index].let { subs ->
                        Row {
                            SubredditIcon(
                                subs.data.display_name,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                            )
                            Text(
                                text = subs.data.display_name.name,
                                modifier = Modifier.clickable {
                                    navController.navigate(com.sofamaniac.reboost.Subreddit(subs.data.display_name.name))
                                })
                        }
                        HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}
