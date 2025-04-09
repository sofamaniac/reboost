package com.sofamaniac.reboost.ui.subreddit

import com.sofamaniac.reboost.reddit.subreddit.HomeRepository


class HomeView : PostsFeedViewerState(title = "Home", repository = HomeRepository())
