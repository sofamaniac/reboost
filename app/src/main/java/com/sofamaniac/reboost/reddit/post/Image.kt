package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.Serializable

@Serializable
data class PostImage(
    val source: PostImageSource,
    val resolutions: List<PostImageSource>
)
