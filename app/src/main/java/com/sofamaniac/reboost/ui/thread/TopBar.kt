package com.sofamaniac.reboost.ui.thread

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.data.remote.dto.comment.Sort

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: ThreadViewModel, scrollBehavior: TopAppBarScrollBehavior?) {
    val navController = LocalNavController.current!!
    val sort: Sort by viewModel.sort.collectAsState()
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() },

                ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        },
        title = {
            Column {
                Text("Comments", style = MaterialTheme.typography.titleMedium)
                Text("${sort}", style = MaterialTheme.typography.labelSmall)
            }
        },
        actions = {
            Icon(Icons.Default.Search, "Search comments")
            SortMenu(viewModel)
            Icon(Icons.Default.MoreVert, "More Options")
        }
    )
}