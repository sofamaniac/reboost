package com.sofamaniac.reboost.ui.subreddit

import android.content.Context
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.post.PostListViewModel
import com.sofamaniac.reboost.reddit.subreddit.SubredditPostsRepository

class SubredditView(val subreddit: String) : PostsFeedViewerState(title = subreddit) {
    override fun viewModelFactory(context: Context): PostListViewModel {
        val apiService = RedditAPI.getApiService(context)
        val repository = SubredditPostsRepository(subreddit, apiService)
        return PostListViewModel(repository)
    }
}