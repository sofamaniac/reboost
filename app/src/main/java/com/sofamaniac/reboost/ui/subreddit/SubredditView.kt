package com.sofamaniac.reboost.ui.subreddit

import com.sofamaniac.reboost.reddit.subreddit.SubredditPostsRepository

class SubredditView(val subreddit: String) : PostsFeedViewerState(
    title = subreddit, repository = SubredditPostsRepository(
        subreddit
    )
)