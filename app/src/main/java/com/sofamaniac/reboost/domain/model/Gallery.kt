package com.sofamaniac.reboost.domain.model

import com.sofamaniac.reboost.data.remote.dto.post.GalleryDataImage
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import kotlinx.serialization.Serializable

@Serializable
data class Gallery(
    val images: List<GalleryDataImage>,
    val mediaMetadata: Map<String, MediaMetadata>
)