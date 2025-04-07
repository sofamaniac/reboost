package com.sofamaniac.reboost.ui.subreddit

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.post.PostListViewModel
import com.sofamaniac.reboost.reddit.subreddit.SavedRepository
import kotlinx.coroutines.launch

class SavedView : PostsFeedViewerState() {
    override fun viewModelFactory(context: Context): PostListViewModel {
        val apiService = RedditAPI.getApiService(context)
        val repository = SavedRepository(apiService)
        return PostListViewModel(repository)
    }

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
