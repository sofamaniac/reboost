package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.Serializable

@Serializable
data class Score(
    val ups: Int,
    val downs: Int,
    val score: Int,
    val upvoteRatio: Double,
    val hideScore: Boolean
)