package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.post.Kind
import com.sofamaniac.reboost.ui.Flair
import kotlinx.coroutines.launch


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PostBody(post: Post, modifier: Modifier = Modifier) {
    when (post.kind()) {
        Kind.Image -> {
            PostImage(post)
        }

        Kind.Video -> {
            PostVideo(post)
        }

        Kind.Link -> {
            Text(
                post.data.url,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier,
            )
        }

        else -> {
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostTitle(post: Post, modifier: Modifier = Modifier, enablePreview: Boolean = true) {
    val titleModifier =
        if (enablePreview && post.thumbnail() != null) modifier.fillMaxSize(fraction = 0.75f) else modifier
    Row(modifier = modifier) {
        Text(post.data.title, style = MaterialTheme.typography.bodyLarge, modifier = titleModifier)
        if (enablePreview && post.thumbnail() != null) {
            GlideImage(
                model = post.thumbnail(),
                contentDescription = "Thumbnail",
                modifier = modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun Post.scoreString(): AnnotatedString {
    val scoreStyle =
        MaterialTheme.typography.titleMedium.toSpanStyle()
    return buildAnnotatedString {
        withStyle(style = scoreStyle) {
            if (score() > 10_000) {
                append((score() / 1000).toString())
                append("k")
            } else {
                append(score().toString())
            }
        }
        append(" · ")
        append("${data.num_comments} comments")
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun View(
    post: Post,
    navController: NavController,
    selected: MutableIntState,
    modifier: Modifier = Modifier,
    showSubredditIcon: Boolean = true,
) {
    val modifier = Modifier.padding(horizontal = 4.dp)
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp) // Space between title, content, buttons
    ) {
        PostHeader(
            post,
            navController,
            selected,
            showSubredditIcon = showSubredditIcon,
            modifier = modifier
        )
        val scope = rememberCoroutineScope()
        Row(modifier = modifier.padding(start = 2.dp)) {
            val enablePreview = post.thumbnail() != null && post.kind() == Kind.Link

            Column(
                modifier = if (enablePreview) modifier.fillMaxWidth(fraction = 0.75f) else modifier,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(post.data.title, style = MaterialTheme.typography.bodyLarge)
                Flair(post.linkFlair())
                Text(post.scoreString(), style = MaterialTheme.typography.bodyMedium)
            }
            if (post.kind() == Kind.Link && post.thumbnail() != null) {
                GlideImage(
                    model = post.thumbnail(),
                    contentDescription = "Thumbnail",
                    modifier = modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        PostBody(post)
        BottomRow(post, modifier)
    }
}