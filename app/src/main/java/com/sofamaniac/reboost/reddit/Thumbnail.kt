/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.net.URI

@Serializable
data class Thumbnail(
    @Contextual val uri: URI,
    val width: Int,
    val height: Int
)