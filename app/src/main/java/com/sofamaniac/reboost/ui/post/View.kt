/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 5:08 PM
 *
 */

package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import com.sofamaniac.reboost.domain.model.Kind
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.ui.Flair
import com.sofamaniac.reboost.ui.SimpleMarkdown
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull


@Composable
internal fun PostBody(post: PostData, modifier: Modifier = Modifier) {
    when (post.kind) {
        Kind.Image -> {
            PostImage(post, Modifier.fillMaxWidth())
        }

        Kind.Video -> {
            PostVideo(post, Modifier.fillMaxWidth())
        }

        Kind.Link -> {
            // TODO check if there is a preview, if not show the link
        }

        Kind.Gallery -> {
            PostGallery(post, Modifier.fillMaxWidth())
        }

        else -> {
            val selftext = post.selftext.html()
            if (selftext.isNotBlank()) {
                SimpleMarkdown(
                    markdown = selftext,
                    maxLines = 6,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
fun PostGallery(post: PostData, modifier: Modifier = Modifier) {
    val current = rememberPagerState(initialPage = 0, pageCount = { post.galleryData.size })
    val minRatio = post.media.mediaMetadata.map { metadata ->
        when (val data = metadata.value) {
            is MediaMetadata.Image -> data.s!!.ratio
            is MediaMetadata.Gif -> data.s!!.ratio
            else -> 1f
        }
    }.min()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(minRatio),
        contentAlignment = Alignment.TopEnd
    ) {
        HorizontalPager(state = current, modifier = Modifier.fillMaxSize()) { page ->
            val mediaId = post.galleryData[page].mediaId
            val metadata: MediaMetadata? = post.media.mediaMetadata[mediaId]
            if (metadata != null) {
                when (metadata) {
                    is MediaMetadata.Image -> ImageView(metadata)
                    is MediaMetadata.Gif -> ImageView(metadata)
                    else -> {
                        Text("No luck my friend (${metadata.javaClass.simpleName})")
                    }
                }
            }
        }
        Text(
            "${current.currentPage + 1}/${post.galleryData.size}",
            modifier = Modifier.background(Color.Black.copy(alpha = 0.6f))
        )
    }
}


@Composable
fun PostInfo(
    post: PostData,
    modifier: Modifier = Modifier,
    enablePreview: Boolean = true,
) {
    val hasThumbnail = enablePreview && post.thumbnail.uri.toHttpUrlOrNull() != null
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
                post.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = titleModifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
            // TODO make clickable
            Flair(post.linkFlair)
            Text(post.scoreString(), style = MaterialTheme.typography.bodyMedium)
        }
        if (hasThumbnail) {
            val thumbnailURL = post.thumbnail.uri.toHttpUrlOrNull()
            val uriHandler = LocalUriHandler.current
            Log.d("PostInfo", "Rendering thumbnail $thumbnailURL")
            AsyncImage(
                model = thumbnailURL,
                contentDescription = post.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.2f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = {
                        uriHandler.openUri(post.url.toString())
                    }),
            )
        }
    }
}

@Composable
fun PostData.scoreString(): AnnotatedString {
    val scoreStyle =
        MaterialTheme.typography.titleMedium.toSpanStyle()
    val score = score.score
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
        append("${numComments} comments")
    }
}

/**
 * Composable function that displays a single post in a column format.
 *
 * This function creates a view for a given [Post], including its header,
 * content, and bottom row of actions. It handles navigation to the full
 * post view.
 *
 * @param post The [Post] data to display.
 * @param selected A [MutableIntState] that holds the index of the current tab.
 * @param modifier Modifier for the root layout of the post.
 * @param showSubredditIcon Whether to display the subreddit icon in the header. Defaults to true.
 * @param clickable Whether the post is clickable to navigate to the full post view. Defaults to true.
 * @param body A composable lambda that defines the main content/body of the post (e.g., text, image). It should manage the horizontal padding itself
 */
@Composable
fun View(
    post: PostData,
    modifier: Modifier = Modifier,
    showSubredditIcon: Boolean = true,
    clickable: Boolean = true,
    visitPost: (PostData) -> Unit = {},
    body: @Composable () -> Unit,
) {
    // We do not apply the padding on the column, but on each of its children except [body]
    // to have images that take the full width
    val modifier = Modifier.padding(horizontal = 16.dp)
    val navController = LocalNavController.current!!
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = clickable, onClick = {
                visitPost(post)
                navController.navigate(com.sofamaniac.reboost.PostRoute(post.permalink))
            }),
        verticalArrangement = Arrangement.spacedBy(4.dp) // Space between title, content, buttons
    ) {
        PostHeader(
            post,
            showSubredditIcon = showSubredditIcon,
            modifier = modifier
        )
        val enablePreview =
            post.thumbnail.uri.toString().isNotEmpty() && post.kind == Kind.Link
        PostInfo(
            post,
            modifier = modifier,
            enablePreview = enablePreview,
        )
        body()
        BottomRow(post, modifier)
    }
}