/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:45 PM
 *
 */

package com.sofamaniac.reboost.ui.subreddit

//import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.reddit.subreddit.SubredditIcon
import com.sofamaniac.reboost.reddit.subreddit.SubredditName

//@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SubredditIcon(
    subreddit: SubredditName,
    icon: SubredditIcon?,
    modifier: Modifier = Modifier
) {
    when (icon) {
        is SubredditIcon.Icon ->
            AsyncImage(
                model = icon.url,
                contentDescription = "${subreddit.name} icon",
                modifier
            )

        is SubredditIcon.Color ->
            Box(
                modifier = modifier
                    .background(
                        Color(icon.color.toColorInt()),
                        shape = CircleShape
                    )
                    .border(width = 1.dp, color = Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("r")
            }

        null ->
            Box(
                modifier = modifier
                    .background(
                        Color("black".toColorInt()),
                        shape = CircleShape
                    )
                    .border(width = 1.dp, color = Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("r")
            }
    }
}