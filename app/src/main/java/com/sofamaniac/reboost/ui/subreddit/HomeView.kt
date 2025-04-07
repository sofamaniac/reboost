package com.sofamaniac.reboost.ui.subreddit

import android.content.Context
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.post.PostListViewModel
import com.sofamaniac.reboost.reddit.subreddit.HomeRepository


class HomeView : PostsFeedViewerState(title = "Home") {
    override fun viewModelFactory(context: Context): PostListViewModel {
        val apiService = RedditAPI.getApiService(context)
        val repository = HomeRepository(apiService)
        return PostListViewModel(repository)
    }
}
