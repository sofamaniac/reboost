/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.dto.post

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
data class PostImageSource(
    val url: String? = null,
    val width: Int = 0,
    val height: Int = 0
)
