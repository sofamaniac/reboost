/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val uri: String,
    val width: Int,
    val height: Int
)