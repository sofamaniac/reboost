/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PostImageSource(
    val url: String? = null,
    val width: Int = 0,
    val height: Int = 0
)
