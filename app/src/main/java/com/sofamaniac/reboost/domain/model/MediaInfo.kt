/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.model

import com.sofamaniac.reboost.data.remote.dto.post.Media
import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import kotlinx.serialization.Serializable

@Serializable
data class MediaInfo(
    val media: Media? = null,
    val mediaEmbed: Map<String, String> = emptyMap(),
    val mediaMetadata: Map<String, MediaMetadata?> = emptyMap(), // Use custom serializer
    val mediaOnly: Boolean = false,
)

