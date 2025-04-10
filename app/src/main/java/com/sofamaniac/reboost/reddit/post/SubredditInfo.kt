package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.Serializable

@Serializable
data class SubredditInfo(
    val subreddit: String,
    val subredditId: String,
    val subredditPrefixed: String,
    val subredditSubscribers: Int,
    val subredditType: String
)