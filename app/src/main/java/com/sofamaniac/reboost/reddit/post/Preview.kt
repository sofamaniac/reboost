package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    val images: List<PostImage> = emptyList(),
    val enabled: Boolean? = false
)
