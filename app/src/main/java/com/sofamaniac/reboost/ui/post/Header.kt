/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.ProfileRoute
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.post.Kind
import com.sofamaniac.reboost.ui.formatElapsedTimeLocalized
import com.sofamaniac.reboost.ui.subreddit.SubredditIcon


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostHeader(
    post: Post,
    selected: MutableIntState,
    modifier: Modifier = Modifier,
    showSubredditIcon: Boolean = true,
) {
    val navController = LocalNavController.current!!
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showSubredditIcon) {
            SubredditIcon(
                post.data.subreddit.name,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable(onClick = {
                        navController.navigate(com.sofamaniac.reboost.SubredditRoute(post.data.subreddit.name.name))
                    })
            )
        }
        val text = buildAnnotatedString {
            withLink(
                LinkAnnotation.Clickable(
                    tag = "Subreddit",
                    styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary)),
                    linkInteractionListener = {
                        navController.navigate(com.sofamaniac.reboost.SubredditRoute(post.data.subreddit.name.name))
                        selected.intValue = 2
                    })
            ) {
                append(post.data.subreddit.name.name)
            }
            append(" · ")
            withLink(
                LinkAnnotation.Clickable(
                    tag = "User",
                    styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary)),
                    linkInteractionListener = {
                        navController.navigate(ProfileRoute(post.data.author.username))
                        selected.intValue = 5
                    })
            ) {
                append(post.data.author.username)
            }
            if (!post.data.domain.contains("reddit") && !post.data.domain.endsWith("redd.it") && post.data.kind != Kind.Self) {
                append(" · ")
                append(post.data.domain)
            }
            append(" · ")
            append(formatElapsedTimeLocalized(post.data.createdAt))
        }
        Text(text, style = MaterialTheme.typography.bodySmall)
        // TODO: take last edit into account
    }
}
