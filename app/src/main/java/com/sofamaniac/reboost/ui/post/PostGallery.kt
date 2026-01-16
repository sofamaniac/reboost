package com.sofamaniac.reboost.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import com.sofamaniac.reboost.domain.model.PostData

@Composable
fun PostGallery(post: PostData, modifier: Modifier = Modifier) {
    val gallery = post.getGalleryData()
    if (gallery.images.isEmpty()) {
        return
    }
    val current = rememberPagerState(initialPage = 0, pageCount = { gallery.images.size })
    val minRatio = gallery.mediaMetadata.map { metadata ->
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
            val mediaId = gallery.images[page].mediaId
            val metadata: MediaMetadata? = gallery.mediaMetadata[mediaId]
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
            "${current.currentPage + 1}/${gallery.images.size}",
            modifier = Modifier.background(Color.Black.copy(alpha = 0.6f))
        )
    }
}