/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit.subreddit

import com.sofamaniac.reboost.reddit.PagedResponse
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.post.Sort
import com.sofamaniac.reboost.reddit.post.PostRepository
import com.sofamaniac.reboost.reddit.post.Timeframe

class SubredditPostsRepository(
    val subreddit: SubredditName,
    sort: Sort = Sort.Best,
    timeframe: Timeframe? = null
) : PostRepository(sort, timeframe) {
    override suspend fun getPosts(after: String): PagedResponse<Post> {
        return makeRequest {
            RedditAPI.service.getSubreddit(
                subreddit = subreddit,
                after = after,
                sort = sort,
                timeframe = timeframe
            )
        }
    }
}
