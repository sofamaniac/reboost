package com.sofamaniac.reboost.reddit.subreddit

import com.sofamaniac.reboost.reddit.PagedResponse
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPIService
import com.sofamaniac.reboost.reddit.Sort
import com.sofamaniac.reboost.reddit.Timeframe
import com.sofamaniac.reboost.reddit.post.PostRepository

class HomeRepository(
    apiService: RedditAPIService,
    sort: Sort = Sort.Best,
    timeframe: Timeframe? = null
) : PostRepository(apiService, sort, timeframe) {
    override suspend fun getPosts(after: String): PagedResponse<Post> {
        return makeRequest { apiService.getHome(sort = sort, timeframe = timeframe, after = after) }
    }
}
