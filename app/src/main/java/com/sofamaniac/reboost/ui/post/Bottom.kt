package com.sofamaniac.reboost.ui.post

import android.util.Log
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
import com.sofamaniac.reboost.BuildConfig
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
private fun UpButton(post: Post) {
    val reddit = RedditAPI.service
    val scope = rememberCoroutineScope()
    val likes = remember { mutableStateOf(post.data.likes) }
    val buttonColor = animateColorAsState(
        targetValue = if (likes.value == true) Color.Red else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = {
        if (likes.value == true) {
            scope.launch { reddit.vote(post.data.name, 0) }
            likes.value = null
        } else {
            scope.launch { reddit.vote(post.data.name, 1) }
            likes.value = true
        }
    }) {
        Icon(Icons.Filled.ThumbUp, "upvote", tint = buttonColor.value)
    }
}

@Composable
private fun DownButton(post: Post) {
    LocalContext.current
    val reddit = RedditAPI.service
    val scope = rememberCoroutineScope()
    val likes = remember { mutableStateOf(post.data.likes) }
    val buttonColor = animateColorAsState(
        targetValue = if (likes.value == false) Color.Blue else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = {
        if (likes.value == false) {
            scope.launch { reddit.vote(post.data.name, 0) }
            likes.value = null
        } else {
            scope.launch { reddit.vote(post.data.name, 1) }
            likes.value = false
        }
    }) {
        Icon(Icons.Filled.ThumbDown, "upvote", tint = buttonColor.value)
    }
}

@Composable
private fun SavedButton(post: Post) {
    LocalContext.current
    val reddit = RedditAPI.service
    val scope = rememberCoroutineScope()
    val saved = remember { mutableStateOf(post.data.saved) }
    val buttonColor = animateColorAsState(
        targetValue = if (saved.value == true) Color.Yellow else Color.Gray,
        label = "button color"
    )
    IconButton(onClick = {
        if (saved.value) {
            scope.launch { reddit.unsave(post.data.name) }
        } else {
            scope.launch { reddit.save(post.data.name) }
        }
        saved.value = !saved.value
    }) {
        if (saved.value) {
            Icon(Icons.Filled.Bookmark, "save", tint = buttonColor.value)
        } else {
            Icon(Icons.Outlined.BookmarkBorder, "save", tint = buttonColor.value)
        }
    }
}

@Composable
fun BottomRow(post: Post, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        UpButton(post)
        DownButton(post)
        SavedButton(post)
        IconButton(onClick = {/*TODO: Comments */ }) {
            Icon(Icons.AutoMirrored.Outlined.Chat, "comments")
        }
        IconButton(onClick = { /*TODO: exit to app*/ }) {
            Icon(Icons.AutoMirrored.Outlined.ExitToApp, "open in app")
        }
        PostOptions(post)
    }
}

@Composable
private fun PostOptions(post: Post, modifier: Modifier = Modifier) {
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
