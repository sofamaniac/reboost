/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit.post

import com.sofamaniac.reboost.reddit.utils.TranscodedVideo
import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    val images: List<PostImage> = emptyList(),
    @Serializable(with = TranscodedVideo::class)
    val redditVideoPreview: RedditVideo? = null,
    val enabled: Boolean? = false
)