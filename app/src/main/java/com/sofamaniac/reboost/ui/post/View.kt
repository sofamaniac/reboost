package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.Subreddit
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Clock
import java.time.Duration
import java.util.Locale


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PostHeader(post: Post, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var subreddit by remember { mutableStateOf(Subreddit()) }
    LaunchedEffect(subreddit) {
        val response = RedditAPI.getApiService(context).getSubInfo(post.data.subreddit)
        if (response.isSuccessful) {
            subreddit = response.body()!!
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlideImage(
            model = subreddit.data.icon_img,
            contentDescription = "${subreddit.data.display_name} icon",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)                       // clip to the circle shape
        ) {
            it.placeholder(Color.Red.toArgb().toDrawable())
        }
        // TODO make subreddit clickable
        Text(
            text = post.data.subreddit,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
        Text(text = "·", style = MaterialTheme.typography.bodySmall)
        Text(text = post.data.author!!, style = MaterialTheme.typography.bodySmall)
        if (!post.data.domain.contains("reddit") && !post.data.domain.endsWith("redd.it") && !post.data.is_self) {
            Text(text = "·", style = MaterialTheme.typography.bodySmall)
            Text(text = post.data.domain, style = MaterialTheme.typography.bodySmall)
        }
        Text(text = "·", style = MaterialTheme.typography.bodySmall)
        Text(
            text = formatElapsedTimeLocalized(post.data.created_utc),
            style = MaterialTheme.typography.bodySmall
        )
        // TODO: take last edit into account
    }
}

private fun formatElapsedTimeLocalized(
    creationDate: Instant,
    locale: Locale = Locale.getDefault()
): String {
    val end = Clock.systemUTC().millis()
    val duration = Duration.ofMillis(kotlin.math.abs(end - creationDate.toEpochMilliseconds()))
    Log.d("formatElapsedTimeLocalized", "${duration}, ${creationDate}, ${end}")

    return when {
        duration.toDays() >= 365 -> String.format(locale, "%dy", duration.toDays() / 365)
        duration.toDays() >= 30 -> String.format(locale, "%dmo", duration.toDays() / 30)
        duration.toDays() > 0 -> String.format(locale, "%dd", duration.toDays())
        duration.toHours() > 0 -> String.format(locale, "%dh", duration.toHours())
        duration.toMinutes() > 0 -> String.format(locale, "%dm", duration.toMinutes())
        else -> String.format(locale, "%ds", duration.seconds)
    }
}

@Composable
private fun PostBody(post: Post, modifier: Modifier = Modifier) {
    Log.d("PostBody", "Post body: ${Json.encodeToString(post)}")
    when (post.data.post_hint ?: "") {
        "image" -> {
            PostImage(post)
        }
        "rich:video", "hosted:video" -> {
            PostVideo(post)
        }

        "link" -> {
            Text(
                post.data.url,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier,
            )
        }

        else -> {
            Column {
                Text("${post.data.post_hint}")
                Text(
                    post.data.selftext,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier,
                )
            }
        }
    }
}

@Composable
private fun upButton(post: Post) {
    val context = LocalContext.current
    val reddit = RedditAPI.getApiService(context)
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
    val context = LocalContext.current
    val reddit = RedditAPI.getApiService(context)
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
    val context = LocalContext.current
    val reddit = RedditAPI.getApiService(context)
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
private fun BottomRow(post: Post, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        upButton(post)
        DownButton(post)
        SavedButton(post)
        IconButton(onClick = {/*TODO: Comments */ }) {
            Icon(Icons.AutoMirrored.Outlined.Chat, "comments")
        }
        IconButton(onClick = { /*TODO: exit to app*/ }) {
            Icon(Icons.AutoMirrored.Outlined.ExitToApp, "open in app")
        }
        IconButton(onClick = {/*TODO*/ }) {
            Icon(Icons.Default.MoreVert, "more")
        }
    }
}

@Composable
fun View(post: Post, modifier: Modifier = Modifier) {
    val modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp), // Add some padding around the post
        verticalArrangement = Arrangement.spacedBy(8.dp) // Space between title, content, buttons
    ) {
        PostHeader(post, modifier)
        Text(post.data.title, style = MaterialTheme.typography.titleMedium, modifier = modifier)
        // TODO add upvote count and comment count
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${post.score()}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "·", style = MaterialTheme.typography.bodySmall)
            Text("${post.data.num_comments} comments", style = MaterialTheme.typography.bodySmall)
        }
        PostBody(post, modifier)
        BottomRow(post, modifier)
    }
}