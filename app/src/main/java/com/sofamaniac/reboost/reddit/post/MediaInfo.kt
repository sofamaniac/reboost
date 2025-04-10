package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.Serializable

@Serializable
data class MediaInfo(
    val media: Media? = null,
    val mediaEmbed: Map<String, String> = emptyMap(),
    val mediaMetadata: Map<String, MediaMetadata>? = null,
    val mediaOnly: Boolean = false,
)