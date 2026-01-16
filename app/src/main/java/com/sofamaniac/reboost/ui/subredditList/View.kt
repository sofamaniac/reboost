/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:45 PM
 *
 */

package com.sofamaniac.reboost.ui.subredditList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.sofamaniac.reboost.ui.subreddit.SubredditIcon
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(drawerState: DrawerState, scrollBehavior: TopAppBarScrollBehavior?) {
        var expanded by remember { mutableStateOf(true) }
        var currentSearch by remember { mutableStateOf("") }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditListViewer(
    navController: NavController,
    viewModel: SubscriptionViewModel = hiltViewModel()
) {
    var isRefreshing by remember { mutableStateOf(false) }

    val subscriptions by viewModel.subscriptions.collectAsState()

    val sortedSubs =
        subscriptions?.sortedBy { it.data.display_name.name.lowercase() } ?: emptyList()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Scaffold(
//        topBar = {
//            TopBar(
//                drawerState = rememberDrawerState(DrawerValue.Closed),
//                scrollBehavior = scrollBehavior
//            )
//        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                scope.launch { viewModel.refresh() }
                isRefreshing = false
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = listState
            ) {
                items(count = sortedSubs.size) { index ->
                    sortedSubs[index].let { subs ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            SubredditIcon(
                                subs.data.display_name,
                                subs.data.icon,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                            )
                            Text(
                                text = subs.data.display_name.name,
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        com.sofamaniac.reboost.SubredditRoute(
                                            subs.data.display_name.name
                                        )
                                    )
                                })
                        }
                    }
                }
            }
        }
    }
}
