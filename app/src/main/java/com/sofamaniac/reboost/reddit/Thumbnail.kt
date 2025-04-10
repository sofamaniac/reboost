package com.sofamaniac.reboost.reddit

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)