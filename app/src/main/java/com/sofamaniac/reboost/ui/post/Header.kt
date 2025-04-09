package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sofamaniac.reboost.Profile
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.Subreddit
import com.sofamaniac.reboost.ui.formatElapsedTimeLocalized


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostHeader(
    post: Post,
    navController: NavController,
    selected: MutableIntState,
    showSubredditIcon: Boolean = true,
    modifier: Modifier = Modifier
) {
    LocalContext.current
    var subreddit by remember { mutableStateOf(Subreddit()) }
    // TODO move to fetch earlier than when needed
    LaunchedEffect(subreddit) {
        val response = RedditAPI.service.getSubInfo(post.data.subreddit)
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
        if (showSubredditIcon) {
            GlideImage(
                model = subreddit.data.icon_img,
                contentDescription = "${subreddit.data.display_name} icon",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = modifier
                    .size(24.dp)
                    .clip(CircleShape)                       // clip to the circle shape
            ) {
                it.placeholder(Color.Red.toArgb().toDrawable())
            }
        }
        val text = buildAnnotatedString {
            withLink(
                LinkAnnotation.Clickable(
                    tag = "Subreddit",
                    styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary)),
                    linkInteractionListener = {
                        navController.navigate(com.sofamaniac.reboost.Subreddit(post.data.subreddit))
                        selected.intValue = 2
                    })
            ) {
                append(post.data.subreddit)
            }
            append(" · ")
            withLink(
                LinkAnnotation.Clickable(
                    tag = "User",
                    styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary)),
                    linkInteractionListener = {
                        navController.navigate(Profile(post.data.author!!))
                        selected.intValue = 5
                    })
            ) {
                append(post.data.author!!)
            }
            if (!post.data.domain.contains("reddit") && !post.data.domain.endsWith("redd.it") && !post.data.is_self) {
                append(" · ")
                append(post.data.domain)
            }
            append(" · ")
            append(formatElapsedTimeLocalized(post.data.created_utc))
        }
        Text(text, style = MaterialTheme.typography.bodySmall)
        // TODO: take last edit into account
    }
}
