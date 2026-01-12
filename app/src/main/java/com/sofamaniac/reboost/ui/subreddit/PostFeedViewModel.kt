package com.sofamaniac.reboost.ui.subreddit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sofamaniac.reboost.accounts.RedditAccount
import com.sofamaniac.reboost.reddit.post.PostRepository
import com.sofamaniac.reboost.reddit.post.PostsSource
import com.sofamaniac.reboost.reddit.post.Sort
import com.sofamaniac.reboost.reddit.post.Timeframe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class PostFeedViewModel(private val repository: PostRepository) : ViewModel() {
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

//    val posts: PagingData<Post> =
//        Pager(
//            config = PagingConfig(
//                pageSize = 100,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = {
//                PostsSource(
//                    repository = repository,
//                )
//            }
//        ).flow
//            .cachedIn(viewModelScope)

    fun refresh() {
        _params.update {
            it.copy(sort = it.sort)
        }
    }

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        _params.update {
            if (it.sort == sort && it.timeframe == timeframe) it
            else {
                repository.updateSort(sort = sort, timeframe = timeframe)
                it.copy(sort = sort, timeframe = timeframe)
            }
        }
    }
}