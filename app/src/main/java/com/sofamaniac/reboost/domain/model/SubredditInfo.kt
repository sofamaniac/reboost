/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.model

import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditId
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditName
import kotlinx.serialization.Serializable

@Serializable
data class SubredditInfo(
    val name: SubredditName,
    val subredditId: SubredditId,
    val subredditPrefixed: String,
    val subredditSubscribers: Int,
    val subredditType: String
)