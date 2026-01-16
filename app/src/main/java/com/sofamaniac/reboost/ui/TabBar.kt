package com.sofamaniac.reboost.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.Home
import com.sofamaniac.reboost.InboxRoute
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.ProfileRoute
import com.sofamaniac.reboost.SearchRoute
import com.sofamaniac.reboost.SubscriptionsRoute

@Composable
fun TabBar(selected: State<Int> ,modifier: Modifier = Modifier) {
    val tabs = listOf(
        Pair(Icons.Filled.Home, Home),
        Pair(Icons.Default.Search, SearchRoute),
        Pair(Icons.AutoMirrored.Outlined.List, SubscriptionsRoute),
        Pair(Icons.Default.Email, InboxRoute),
        Pair(Icons.Filled.Person, ProfileRoute("me"))
    )
    val navController = LocalNavController.current!!
    PrimaryTabRow(selectedTabIndex = selected.value, modifier = modifier.navigationBarsPadding()) {
        for ((index, tab) in tabs.withIndex()) {
            Tab(
                selected = selected.value == index,
                onClick = {
                    navController.navigate(tab.second) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(tab.first, contentDescription = tab.second.title) },
            )
        }
    }
}

