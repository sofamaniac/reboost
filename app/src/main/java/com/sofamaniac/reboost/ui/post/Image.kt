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
import com.sofamaniac.reboost.reddit.post.Post
import org.apache.commons.text.StringEscapeUtils

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostImage(post: Post, modifier: Modifier = Modifier) {
    if (post.data.preview?.images?.isNotEmpty() == true) {
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
            it.thumbnail(Glide.with(context).load(post.data.thumbnail))
            it.placeholder(Color.Gray.toArgb().toDrawable())
            it.load(url)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FromMetadata(post: Post, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val images = post.data.preview!!
    val image = images.images[0].source
    val url = StringEscapeUtils.unescapeHtml4(image.url)
    val x = image.width
    val y = image.height
    GlideImage(
        model = url,
        contentDescription = post.data.title,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(x.toFloat() / y.toFloat()),
        contentScale = ContentScale.FillWidth,
    ) {
        if (post.data.thumbnail != null) {
            it.thumbnail(Glide.with(context).load(post.data.thumbnail))
        }
        it.placeholder(Color.Gray.toArgb().toDrawable())
        it.load(url)
    }
}
