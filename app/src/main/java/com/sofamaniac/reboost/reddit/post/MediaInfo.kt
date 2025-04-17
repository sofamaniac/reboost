/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.Serializable

@Serializable
data class MediaInfo(
    val media: Media? = null,
    val mediaEmbed: Map<String, String> = emptyMap(),
    val mediaMetadata: Map<String, MediaMetadata?> = emptyMap(), // Use custom serializer
    val mediaOnly: Boolean = false,
)

