package com.sofamaniac.reboost.reddit.subreddit

import com.sofamaniac.reboost.reddit.PagedResponse
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.Sort
import com.sofamaniac.reboost.reddit.Timeframe
import com.sofamaniac.reboost.reddit.post.Post
import com.sofamaniac.reboost.reddit.post.PostRepository

class SubredditPostsRepository(
    val subreddit: String,
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
