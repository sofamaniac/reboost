package com.sofamaniac.reboost.reddit.subreddit

import com.sofamaniac.reboost.reddit.PagedResponse
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.Sort
import com.sofamaniac.reboost.reddit.Timeframe
import com.sofamaniac.reboost.reddit.post.PostRepository

class HomeRepository(
    sort: Sort = Sort.Best,
    timeframe: Timeframe? = null
) : PostRepository(sort, timeframe) {
    override suspend fun getPosts(after: String): PagedResponse<Post> {
        return makeRequest {
            RedditAPI.service.getHome(
                sort = sort,
                timeframe = timeframe,
                after = after
            )
        }
    }
}
