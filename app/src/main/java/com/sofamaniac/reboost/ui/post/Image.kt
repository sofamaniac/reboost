/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

//import com.bumptech.glide.Glide
//import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
//import com.bumptech.glide.integration.compose.GlideImage
import android.util.Log
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.post.MediaMetadata
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.lang.Float.max

//@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostImage(post: Post, modifier: Modifier = Modifier) {
    Log.d("PostImage", "Rendering image")
    if (!post.data.preview.images.isEmpty()) {
        FromMetadata(post, modifier)
    } else {
        val context = LocalContext.current
        val url = post.data.url
        AsyncImage(
            model = url,
            contentDescription = post.data.title,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Fit,
        )
//        GlideImage(
//            model = url,
//            contentDescription = "Image from URL",
//            modifier = Modifier
//                .fillMaxSize()
//                .wrapContentHeight(),
//            contentScale = ContentScale.Fit,
//        ) {
//            val thumbnailURL = post.data.thumbnail.uri
//            if (thumbnailURL != null) {
//                it.thumbnail(Glide.with(context).load(thumbnailURL))
//            }
//            it.placeholder(Color.Gray.toArgb().toDrawable())
//            it.load(url.toURI())
//        }
    }
}

//@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FromMetadata(post: Post, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val images = post.data.preview
    val image = images.images[0].source
    val url = image.url
    val x = image.width
    val y = max(image.height.toFloat(), 1.0.toFloat())
    val thumbnailURL = post.data.thumbnail.uri.toHttpUrlOrNull()?.toUrl()
    Log.d("ImageFromMetadata", "Rendering image from metadata $x x $y ($url)")
    AsyncImage(
        model = url.toString(),
        contentDescription = post.data.title,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(x.toFloat() / y),
        contentScale = ContentScale.Fit,
    )
//    GlideImage(
//        model = url,
//        contentDescription = post.data.title,
//        modifier = Modifier
//            .fillMaxWidth()
//            .aspectRatio(x.toFloat() / y.toFloat()),
//        contentScale = ContentScale.Fit,
//    )
    //{
//        if (thumbnailURL != null) {
//            it.thumbnail(Glide.with(context).load(thumbnailURL))
//        }
    // it.placeholder(Color.Gray.toArgb().toDrawable())
    //}
}

//@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageView(metadata: MediaMetadata.Image, modifier: Modifier = Modifier) {
    val url = metadata.s!!.url.toString()
    val ratio = metadata.s.width.toFloat() / metadata.s.height.toFloat()
    Log.d("ImageView", "Image: ratio $ratio")
    AsyncImage(
        model = url,
        contentDescription = "Image",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio),
        contentScale = ContentScale.Fit,
    )
//    GlideImage(
//        model = url,
//        contentDescription = "Image",
//        modifier = Modifier
//            .fillMaxWidth()
//            //.aspectRatio(ratio)
//    )
}

//@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageView(metadata: MediaMetadata.Gif, modifier: Modifier = Modifier) {
    val url = metadata.s!!.gifUrl.toString()
    val ratio = metadata.s.width.toFloat() / metadata.s.height.toFloat()
    // TODO choose the proper one based on resolution
    val previewUrl = metadata.preview[0].url.toString()
    val context = LocalContext.current
    Log.d("ImageView", "Gif: ratio $ratio")
    AsyncImage(
        model = url,
        contentDescription = "Gif",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio),
        contentScale = ContentScale.Fit,
    )
//    GlideImage(
//        model = url,
//        contentDescription = "Image",
//        modifier = Modifier
//            .fillMaxWidth(),
//            //.aspectRatio(ratio),
//        requestBuilderTransform = {
//            it.thumbnail(Glide.with(context).load(previewUrl))
//        }
//    )
}

