package com.sofamaniac.reboost.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.sofamaniac.reboost.reddit.Identity
import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.RedditAPIService
import com.sofamaniac.reboost.reddit.Subreddit
import com.sofamaniac.reboost.reddit.Thing
import com.sofamaniac.reboost.ui.post.PostViewModel
import com.sofamaniac.reboost.ui.post.PostsSource
import com.sofamaniac.reboost.ui.post.View
import kotlinx.coroutines.launch
import retrofit2.Response

data class PagedResponse<T : Thing<Any>>(
    val data: List<T> = emptyList<T>(),
    val after: String? = null,
    val total: Int = 0
)

class SubredditRepository(
    protected val apiService: RedditAPIService,
) {
    protected suspend fun makeRequest(
        request: suspend () -> Response<Listing<Subreddit>>
    ): PagedResponse<Subreddit> {
        val response = request()
        if (response.isSuccessful) {
            Log.d("makeRequest", "code ${response.code()}")
            val listing = response.body()
            listing?.let {
                return PagedResponse(
                    data = it.data.children,
                    after = it.after(),
                    total = it.size()
                )
            }
            return PagedResponse()
        }
        Log.e("makeRequest", "Error making request : ${response.errorBody()}")
        return PagedResponse()
    }

    open suspend fun getSubreddits(after: String): PagedResponse<Subreddit> {
        return makeRequest { apiService.getSubreddits(after = after) }
    }

    suspend fun getUser(): Identity? {
        return apiService.getIdentity().body()
    }
}

class SubredditSource(
    private val repository: SubredditRepository
) : PagingSource<String, Subreddit>() {

    override fun getRefreshKey(state: PagingState<String, Subreddit>): String? {
        return ""
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Subreddit> {
        val subs = if (params.key != null) {
            getSubreddits(params.key!!)
        } else {
            PagedResponse()
        }
        return LoadResult.Page(
            prevKey = null,
            nextKey = subs.after,
            data = subs.data
        )
    }

    private suspend fun getSubreddits(after: String): PagedResponse<Subreddit> {
        return repository.getSubreddits(after)
    }
}

class SubsViewModel(repository: SubredditRepository) : ViewModel() {
    lateinit var state: LazyListState
    fun isStateInitialized(): Boolean {
        return ::state.isInitialized
    }

    val data = Pager(
        config = PagingConfig(pageSize = 100),
        initialKey = "",
        pagingSourceFactory = {
            SubredditSource(repository)
        }
    ).flow.cachedIn(viewModelScope)
    var user: Identity? = null

    init {
        viewModelScope.launch {
            user = repository.getUser()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditsViewer(viewModel: SubsViewModel) {
    val posts = viewModel.data.collectAsLazyPagingItems()
    if (!viewModel.isStateInitialized()) {
        viewModel.state = rememberLazyListState()
    }
    PullToRefreshBox(
        isRefreshing = posts.loadState.refresh == LoadState.Loading,
        onRefresh = {
            posts.refresh()
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = viewModel.state,
        ) {
            items(count = posts.itemCount) { index ->
                posts[index]?.let { subs ->
                    Text(text = subs.data.display_name)
                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
