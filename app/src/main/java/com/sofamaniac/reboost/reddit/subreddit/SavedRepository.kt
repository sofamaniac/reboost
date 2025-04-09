package com.sofamaniac.reboost.reddit.subreddit

import com.sofamaniac.reboost.reddit.PagedResponse
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.Sort
import com.sofamaniac.reboost.reddit.Timeframe
import com.sofamaniac.reboost.reddit.post.PostRepository

class SavedRepository(
    sort: Sort = Sort.Best,
    timeframe: Timeframe? = null
) : PostRepository(sort, timeframe) {
    private lateinit var username: String
    override suspend fun getPosts(after: String): PagedResponse<Post> {
        if (!this::username.isInitialized) {
            val response = RedditAPI.service.getIdentity()
            if (!response.isSuccessful) return PagedResponse()
            username = response.body()!!.username
        }
        return makeRequest {
            RedditAPI.service.getSaved(
                after = after,
                user = username,
                sort = sort,
                timeframe = timeframe
            )
        }
    }
}
