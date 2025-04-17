/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.BottomBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    showSettings: Boolean = true,
    showSort: Boolean = true
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(Icons.Default.Menu, "Open Drawer")
            }
        },
        actions = {}
    )
}

@Composable
fun ProfileInfo(modifier: Modifier = Modifier) {
    Text("WIP")
}

@Composable
fun ProfileView(user: String, drawerState: DrawerState, modifier: Modifier = Modifier) {
    var currentTab = rememberPagerState(initialPage = 0, pageCount = { 5 })
    val tabs = listOf("Overview", "About", "Posts", "Comments", "Gilded")
    var offset by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBar(drawerState) },
        modifier = modifier,
        bottomBar = {
            BottomBar(selected = remember { mutableIntStateOf(0) })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            ProfileInfo()
            ScrollableTabRow(
                selectedTabIndex = currentTab.currentPage,
                modifier = Modifier.fillMaxWidth(),
                edgePadding = 0.dp
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = index == currentTab.currentPage,
                        onClick = {
                            scope.launch { currentTab.animateScrollToPage(index) }
                        },
                        text = { Text(tab) })
                }
            }
            HorizontalPager(state = currentTab, modifier = Modifier.fillMaxSize()) {
                when (it) {
                    0 -> Text("Overview")
                    1 -> Text("About")
                    2 -> Text("Posts")
                    3 -> Text("Comments")
                    4 -> Text("Gilded")
                }
            }
        }
    }
}