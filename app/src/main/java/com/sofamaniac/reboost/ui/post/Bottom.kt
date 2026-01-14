/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.activity.compose.ReportDrawn
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import com.sofamaniac.reboost.BuildConfig
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.PostRoute
import com.sofamaniac.reboost.data.remote.api.RedditAPI
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PostData
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
private fun UpButton(post: PostData, reddit: RedditAPIService) {
    val scope = rememberCoroutineScope()
    val likes = remember { mutableStateOf(post.relationship.liked) }
    val buttonColor = animateColorAsState(
        targetValue = if (likes.value == true) Color.Red else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = {
        if (likes.value == true) {
            scope.launch { reddit.vote(post.name, 0) }
            likes.value = null
        } else {
            scope.launch { reddit.vote(post.name, 1) }
            likes.value = true
        }
    }) {
        Icon(Icons.Filled.ThumbUp, "upvote", tint = buttonColor.value)
    }
}

@Composable
private fun DownButton(post: PostData, reddit: RedditAPIService) {
    val scope = rememberCoroutineScope()
    var likes by remember { mutableStateOf(post.relationship.liked) }
    val buttonColor = animateColorAsState(
        targetValue = if (likes == false) Color.Blue else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = {
        if (likes == false) {
            scope.launch { reddit.vote(post.name, 0) }
            likes = null
        } else {
            scope.launch { reddit.vote(post.name, 1) }
            likes = false
        }
    }) {
        Icon(Icons.Filled.ThumbDown, "upvote", tint = buttonColor.value)
    }
}

@Composable
private fun SavedButton(post: PostData, reddit: RedditAPIService) {
    val scope = rememberCoroutineScope()
    var saved by remember { mutableStateOf(post.relationship.saved) }
    val buttonColor = animateColorAsState(
        targetValue = if (saved) Color.Yellow else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = {
        if (saved) {
            scope.launch { reddit.unsave(post.name) }
        } else {
            scope.launch { reddit.save(post.name) }
        }
        saved = !saved
    }) {
        if (saved) {
            Icon(Icons.Filled.Bookmark, "save", tint = buttonColor.value)
        } else {
            Icon(Icons.Outlined.BookmarkBorder, "save", tint = buttonColor.value)
        }
    }
}

@Composable
fun BottomRow(post: PostData, modifier: Modifier = Modifier) {
    val navController = LocalNavController.current!!
    val uriHandler = LocalUriHandler.current
    val service = RedditAPI()
    service.init(LocalContext.current)
    val reddit = service.service
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        UpButton(post, reddit)
        DownButton(post, reddit)
        SavedButton(post, reddit)
        IconButton(onClick = {
            navController.navigate(PostRoute(post.permalink))
        }) {
            Icon(Icons.AutoMirrored.Outlined.Chat, "comments")
        }
        IconButton(onClick = {
            uriHandler.openUri(post.url.toString())
        }) {
            Icon(Icons.AutoMirrored.Outlined.ExitToApp, "open in app")
        }
        PostOptions(post)
    }
}

@Composable
private fun PostOptions(post: PostData, modifier: Modifier = Modifier) {
    var showOptions by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { showOptions = true }) {
            Icon(Icons.Default.MoreVert, "more")
        }
        DropdownMenu(expanded = showOptions, onDismissRequest = { showOptions = false }) {
            if (BuildConfig.DEBUG) {
                DropdownMenuItem(text = { Text("Post content") }, onClick = {
                    Log.d("Post", Json.encodeToString(post))
                })
            }
        }
    }
}
