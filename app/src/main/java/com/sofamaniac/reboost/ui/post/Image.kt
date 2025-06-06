/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.post.MediaMetadata
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostImage(post: Post, modifier: Modifier = Modifier) {
    if (post.data.preview.images.isNotEmpty()) {
        FromMetadata(post, modifier)
    } else {
        val context = LocalContext.current
        val url = post.data.url
        GlideImage(
            model = url,
            contentDescription = "Image from URL",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        ) {
            val thumbnailURL = post.data.thumbnail.uri.toHttpUrlOrNull()
            if (thumbnailURL != null) {
                it.thumbnail(Glide.with(context).load(thumbnailURL.toUrl()))
            }
            it.placeholder(Color.Gray.toArgb().toDrawable())
            it.load(url)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FromMetadata(post: Post, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val images = post.data.preview
    val image = images.images[0].source
    val url = image.url
    val x = image.width
    val y = image.height
    val thumbnailURL = post.data.thumbnail.uri.toHttpUrlOrNull()?.toUrl()
    GlideImage(
        model = url,
        contentDescription = post.data.title,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(x.toFloat() / y.toFloat()),
        contentScale = ContentScale.FillWidth,
    ) {
        if (thumbnailURL != null) {
            it.thumbnail(Glide.with(context).load(thumbnailURL))
        }
        it.placeholder(Color.Gray.toArgb().toDrawable())
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageView(metadata: MediaMetadata.Image, modifier: Modifier = Modifier) {
    val url = metadata.s!!.url.toString()
    val ratio = metadata.s.width.toFloat() / metadata.s.height.toFloat()
    GlideImage(
        model = url,
        contentDescription = "Image",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio)
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageView(metadata: MediaMetadata.Gif, modifier: Modifier = Modifier) {
    val url = metadata.s!!.gifUrl.toString()
    val ratio = metadata.s.width.toFloat() / metadata.s.height.toFloat()
    // TODO choose the proper one based on resolution
    val previewUrl = metadata.preview[0].url.toString()
    val context = LocalContext.current
    GlideImage(
        model = url,
        contentDescription = "Image",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio),
        requestBuilderTransform = {
            it.thumbnail(Glide.with(context).load(previewUrl))
        }
    )
}

