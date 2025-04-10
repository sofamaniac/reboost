package com.sofamaniac.reboost.reddit.post

import com.sofamaniac.reboost.reddit.utils.MapMediaMetadataOptionSerializer
import kotlinx.serialization.Serializable

@Serializable
data class MediaInfo(
    val media: Media? = null,
    val mediaEmbed: Map<String, String> = emptyMap(),
    @Serializable(with = MapMediaMetadataOptionSerializer::class)
    val mediaMetadata: Map<String, MediaMetadata?>? = null, // Use custom serializer
    val mediaOnly: Boolean = false,
)

