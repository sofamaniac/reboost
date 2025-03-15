package com.sofamaniac.reboost

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp


@Composable
fun PostItem(post: Post, modifier: Modifier = Modifier, currentPost: MutableState<Post?>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Add some padding around the post
        verticalArrangement = Arrangement.spacedBy(8.dp) // Space between title, content, buttons
    ) {

        Text(
            text = post.data.title,
            style = MaterialTheme.typography.headlineSmall, // Adjust text style
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { currentPost.value = post })
        )

        var isShown = false
        when (post.data.post_hint ?: "") {
            "image" -> {
                MyImage(post.data.url)
                isShown = true
            }
            "link" -> {
                Text(post.data.url)
                isShown = true
            }
            else -> {
                Text(post.data.selftext)
                isShown = true
            }
        }

        // Voting Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between the buttons
        ) {
            Button(onClick = { /* TODO: Upvote */ }) {
                Text("Up")
            }
            Button(onClick = { /* TODO: Downvote */ }) {
                Text("Down")
            }
        }
    }
}