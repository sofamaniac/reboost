package com.sofamaniac.reboost.reddit.post

import com.sofamaniac.reboost.reddit.subreddit.SubredditId
import com.sofamaniac.reboost.reddit.subreddit.SubredditName
import kotlinx.serialization.Serializable

@Serializable
data class SubredditInfo(
    val name: SubredditName,
    val subredditId: SubredditId,
    val subredditPrefixed: String,
    val subredditSubscribers: Int,
    val subredditType: String
)