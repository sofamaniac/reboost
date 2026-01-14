/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:45 PM
 *
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
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.ProfileRoute
import com.sofamaniac.reboost.domain.model.Kind
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.formatElapsedTimeLocalized
import com.sofamaniac.reboost.ui.subreddit.SubredditIcon


//@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostHeader(
    post: PostData,
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
                post.subreddit.name,
                post.subredditDetails?.icon,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable(onClick = {
                        navController.navigate(com.sofamaniac.reboost.SubredditRoute(post.subreddit.name.name))
                    })
            )
        }
        val text = buildAnnotatedString {
            withLink(
                LinkAnnotation.Clickable(
                    tag = "Subreddit",
                    styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary)),
                    linkInteractionListener = {
                        navController.navigate(com.sofamaniac.reboost.SubredditRoute(post.subreddit.name.name))
                    })
            ) {
                append(post.subreddit.name.name)
            }
            append(" · ")
            withLink(
                LinkAnnotation.Clickable(
                    tag = "User",
                    styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary)),
                    linkInteractionListener = {
                        navController.navigate(ProfileRoute(post.author.username))
                    })
            ) {
                append(post.author.username)
            }
            if (!post.domain.contains("reddit") && !post.domain.endsWith("redd.it") && post.kind != Kind.Self) {
                append(" · ")
                append(post.domain)
            }
            append(" · ")
            append(formatElapsedTimeLocalized(post.createdAt))
        }
        Text(text, style = MaterialTheme.typography.bodySmall)
        // TODO: take last edit into account
    }
}
