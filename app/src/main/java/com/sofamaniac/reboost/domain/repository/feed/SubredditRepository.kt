/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Thing.Post
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditName
import com.sofamaniac.reboost.domain.model.PagedResponse
import jakarta.inject.Inject

class SubredditPostsRepository @Inject constructor(
    api: RedditAPIService,
) : PostRepository(api), FeedRepository {
    private var currentSubreddit: SubredditName? = null

    fun updateSubreddit(subreddit: SubredditName) {
        currentSubreddit = subreddit
    }

    override suspend fun getPosts(after: String, sort: Sort, timeframe: Timeframe?): PagedResponse<Post> {
        val subreddit = currentSubreddit ?: return PagedResponse()
        return makeRequest {
            api.getSubreddit(
                subreddit = subreddit,
                after = after,
                sort = sort,
                timeframe = timeframe
            )
        }
    }
}
