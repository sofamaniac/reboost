/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PostImage(
    val source: PostImageSource,
    val resolutions: List<PostImageSource>,
    val id: String,
    val variants: PostImageVariants? = null,
)

@Serializable
data class PostImageVariants(
    val obfuscated: PostImageSource? = null,
    val nsfw: PostImageSource? = null,
)