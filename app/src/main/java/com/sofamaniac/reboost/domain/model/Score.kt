/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Score(
    val ups: Int,
    val downs: Int,
    val score: Int,
    val upvoteRatio: Double,
    val hideScore: Boolean
)