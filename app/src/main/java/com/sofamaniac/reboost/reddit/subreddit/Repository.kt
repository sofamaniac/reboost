package com.sofamaniac.reboost.reddit.subreddit

import com.sofamaniac.reboost.reddit.PagedResponse
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPIService
import com.sofamaniac.reboost.reddit.Sort
import com.sofamaniac.reboost.reddit.Timeframe
import com.sofamaniac.reboost.reddit.post.PostRepository

class SubredditPostsRepository(
    val subreddit: String,
    apiService: RedditAPIService,
    sort: Sort = Sort.Best,
    timeframe: Timeframe? = null
) : PostRepository(apiService, sort, timeframe) {
    private lateinit var username: String
    override suspend fun getPosts(after: String): PagedResponse<Post> {
        if (!this::username.isInitialized) {
            val response = apiService.getIdentity()
            if (!response.isSuccessful) return PagedResponse()
            username = response.body()!!.username
        }
        return makeRequest {
            apiService.getSubreddit(
                subreddit = subreddit,
                after = after,
                sort = sort,
                timeframe = timeframe
            )
        }
    }
}
