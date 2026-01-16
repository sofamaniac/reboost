/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:10 PM
 *
 */

package com.sofamaniac.reboost.ui.subreddit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.local.entities.toEntity
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.domain.model.RedditAccount
import com.sofamaniac.reboost.domain.repository.feed.PostRepository
import com.sofamaniac.reboost.domain.repository.feed.PostsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class PostFeedViewModel(private val repository: PostRepository, private val visitedPostsDao: VisitedPostsDao) : ViewModel() {
    data class FeedParams(
        val account: RedditAccount,
        val sort: Sort,
        val timeframe: Timeframe?

    )

    private val _params = MutableStateFlow(
        FeedParams(
            account = RedditAccount.anonymous(),
            sort = Sort.Best,
            timeframe = null,
        )
    )
    val params: StateFlow<FeedParams> = _params.asStateFlow()

    private var postsSource: PostsSource? = null
    var data = Pager(
        config = PagingConfig(pageSize = 100),
        initialKey = "",
        pagingSourceFactory = { PostsSource(repository).also { postsSource = it } }
    )
        .flow.cachedIn(
            viewModelScope
        )

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        _params.update {
            if (it.sort == sort && it.timeframe == timeframe) it
            else {
                postsSource?.setSort(sort, timeframe)
                it.copy(sort = sort, timeframe = timeframe)
            }
        }
    }

    fun visitPost(post: PostData) {
        viewModelScope.launch(Dispatchers.IO) {
            visitedPostsDao.insert(post.toEntity())
            Log.d("PostFeedViewModel", "visitPost: Post visited (${post.id.id})")
        }
    }
}