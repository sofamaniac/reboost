package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
data class PostImageSource(
    @Contextual val url: URL? = null,
    val width: Int = 0,
    val height: Int = 0
)
