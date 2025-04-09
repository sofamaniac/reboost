package com.sofamaniac.reboost

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomBar(
    navController: NavController,
    selected: MutableIntState,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        Pair(Icons.Filled.Home, Home),
        Pair(Icons.Default.Search, Search),
        Pair(Icons.AutoMirrored.Outlined.List, Subscriptions),
        Pair(Icons.Default.Email, Inbox),
        Pair(Icons.Filled.Person, Profile("me"))
    )
    CustomNavigationBar(modifier = Modifier.fillMaxWidth()) {
        for ((index, tab) in tabs.withIndex()) {
            TabButton(index, navController, selected, tab.first, tab.second, tab.second.title)
        }
    }
}

@Composable
private fun TabButton(
    index: Int,
    navController: NavController,
    selected: MutableIntState,
    icon: ImageVector,
    route: Any,
    description: String,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = {
            selected.intValue = index
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = if (selected.intValue == index) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}

@Composable
private fun CustomNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .defaultMinSize(minHeight = 16.dp) // Change minHeight to your desired height
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}
