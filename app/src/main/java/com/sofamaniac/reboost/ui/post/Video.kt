/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.sofamaniac.reboost.reddit.Post

@OptIn(UnstableApi::class)
@Composable
fun PostVideo(post: Post, modifier: Modifier = Modifier) {
    val media = post.data.media.media
    if (media != null && media.reddit_video != null) {
        val context = LocalContext.current
        val mediaItem = MediaItem.fromUri(media.reddit_video.fallback_url?.toString() ?: "")
        val width = media.reddit_video.width
        val height = media.reddit_video.height
        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                // Set MediaSource to ExoPlayer
                setMediaItem(mediaItem)
                playWhenReady = true
                prepare()
            }
        }
        Log.d("PostVideo", "${post.data}")

        // Manage lifecycle events
        DisposableEffect(Unit) {
            onDispose {
                exoPlayer.release()
            }
        }
        Box {
            Text(text = post.data.url.toString())
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        setShowNextButton(false)
                        setShowRewindButton(false)
                        setShowFastForwardButton(false)
                        setShowPreviousButton(false)
                    }
                },
                modifier = Modifier
                    .aspectRatio(width.toFloat() / height.toFloat())
                    .fillMaxWidth()
            )
        }
    }
}

