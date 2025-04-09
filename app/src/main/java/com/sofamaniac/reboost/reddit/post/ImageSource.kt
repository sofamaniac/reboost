package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.Serializable

@Serializable
data class PostImageSource(
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0
)
