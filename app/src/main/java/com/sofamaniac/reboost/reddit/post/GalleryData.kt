/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GalleryData(val items: List<GalleryDataImage>)

@Serializable
data class GalleryDataImage(
    @SerialName("media_id") val mediaId: String,
    @SerialName("id") val id: String,
)
