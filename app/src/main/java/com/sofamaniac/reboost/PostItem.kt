package com.sofamaniac.reboost

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.reddit.Post
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun PostHeader(post: Post, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        // TODO add subreddit icon
        // TODO make subreddit clickable
        Text(text = post.data.subreddit, style = MaterialTheme.typography.bodySmall)
        Text(text = " · ", style = MaterialTheme.typography.bodySmall)
        Text(text = post.data.author_fullname, style = MaterialTheme.typography.bodySmall)
        Text(text = " · ", style = MaterialTheme.typography.bodySmall)
        // TODO: add website
        // TODO: add date
        //Text(text = post.data., style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun PostBody(post: Post, modifier: Modifier = Modifier) {
    Log.d("PostBody", "Post body: ${Json.encodeToString(post)}")
    when (post.data.post_hint ?: "") {
        "image" -> {
            MyImage(post.data.url)
        }
        "link" -> {
            Text(post.data.url, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        else -> {
            Text(post.data.selftext, style = MaterialTheme.typography.bodyMedium, maxLines = 6, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun BottomRow(post: Post, modifier: Modifier = Modifier) {
    Row {
        Button(onClick = { /* TODO: Upvote */ }) {
            Text("Up")
        }
        Button(onClick = { /* TODO: Downvote */ }) {
            Text("Down")
        }
    }
}

@Composable
fun PostItem(post: Post, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Add some padding around the post
        verticalArrangement = Arrangement.spacedBy(8.dp) // Space between title, content, buttons
    ) {
        PostHeader(post)
        Text(post.data.title, style = MaterialTheme.typography.titleMedium)
        // TODO add upvote count and comment count
        Row {
            Text("${post.score()}", style = MaterialTheme.typography.bodyMedium)
            Text(text = " · ", style = MaterialTheme.typography.bodySmall)
            Text("${post.data.num_comments} comments", style = MaterialTheme.typography.bodySmall)
        }
        PostBody(post)
        BottomRow(post)
    }
}