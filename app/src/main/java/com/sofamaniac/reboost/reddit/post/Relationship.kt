/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.Serializable

@Serializable
data class Relationship(
    val clicked: Boolean,
    val visited: Boolean,
    val saved: Boolean,
    val liked: Boolean?
)