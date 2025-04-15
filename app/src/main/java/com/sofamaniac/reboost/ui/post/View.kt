package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.post.Kind
import com.sofamaniac.reboost.ui.Flair
import com.sofamaniac.reboost.ui.SimpleMarkdown
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun PostBody(post: Post, modifier: Modifier = Modifier) {
    when (post.data.kind) {
        Kind.Image -> {
            PostImage(post)
        }

        Kind.Video -> {
            PostVideo(post)
        }

        Kind.Link -> {
            Text(
                post.data.url.toString(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier,
            )
        }

        else -> {
            if (post.data.selftext.html().isNotEmpty()) {
                SimpleMarkdown(
                    markdown = post.data.selftext.html(),
                    maxLines = 6,
                    modifier = modifier,
                )
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostInfo(
    post: Post,
    modifier: Modifier = Modifier,
    enablePreview: Boolean = true,
) {
    val hasThumbnail = enablePreview && post.data.thumbnail.uri.toHttpUrlOrNull() != null
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 2.dp, end = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val width = if (hasThumbnail) 0.8f else 1f
            val titleModifier = Modifier.fillMaxWidth(fraction = width)
            Text(
                post.data.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = titleModifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
            // TODO make clickable
            Flair(post.data.linkFlair)
            Text(post.scoreString(), style = MaterialTheme.typography.bodyMedium)
        }
        if (hasThumbnail) {
            val thumbnailURL = post.data.thumbnail.uri.toHttpUrlOrNull()
            val uriHandler = LocalUriHandler.current
            GlideImage(
                model = thumbnailURL?.toUrl(),
                contentDescription = "Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.2f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = {
                        uriHandler.openUri(post.data.url.toString())
                    }),

                )
        }
    }
}

@Composable
fun Post.scoreString(): AnnotatedString {
    val scoreStyle =
        MaterialTheme.typography.titleMedium.toSpanStyle()
    val score = data.score.score
    return buildAnnotatedString {
        withStyle(style = scoreStyle) {
            if (score > 10_000) {
                append((score / 1000).toString())
                append("k")
            } else {
                append(score.toString())
            }
        }
        append(" · ")
        append("${data.numComments} comments")
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun View(
    post: Post,
    selected: MutableIntState,
    modifier: Modifier = Modifier,
    showSubredditIcon: Boolean = true,
    clickable: Boolean = true,
) {
    val modifier = Modifier.padding(horizontal = 4.dp)
    val navController = LocalNavController.current!!
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = clickable, onClick = {
                navController.navigate(com.sofamaniac.reboost.PostRoute(post.data.permalink))
            }),
        verticalArrangement = Arrangement.spacedBy(4.dp) // Space between title, content, buttons
    ) {
        PostHeader(
            post,
            selected,
            showSubredditIcon = showSubredditIcon,
            modifier = modifier
        )
        val enablePreview =
            post.data.thumbnail.uri.toString().isNotEmpty() && post.data.kind == Kind.Link
        PostInfo(
            post,
            modifier = modifier,
            enablePreview = enablePreview,
        )
        PostBody(post)
        BottomRow(post, modifier)
    }
}