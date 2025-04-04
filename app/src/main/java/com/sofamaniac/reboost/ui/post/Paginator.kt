package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPIService
import com.sofamaniac.reboost.reddit.Sort
import com.sofamaniac.reboost.reddit.Thing
import com.sofamaniac.reboost.reddit.Timeframe
import retrofit2.Response


data class PagedResponse<T : Thing<Any>>(
    val data: List<T> = emptyList<T>(),
    val after: String? = null,
    val total: Int = 0
)

abstract class PostRepository(
    protected val apiService: RedditAPIService,
    var sort: Sort = Sort.Best,
    var timeframe: Timeframe? = null
) {
    protected suspend fun makeRequest(
        request: suspend () -> Response<Listing<Post>>
    ): PagedResponse<Post> {
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

    open suspend fun getPosts(after: String): PagedResponse<Post> {
        return makeRequest { apiService.getHome(sort = sort, timeframe = timeframe, after = after) }
    }

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        this.sort = sort
        this.timeframe = timeframe
    }

    suspend fun getUser(): Identity? {
        return apiService.getIdentity().body()
    }
}

class PostsSource(
    private val repository: PostRepository
) : PagingSource<String, Post>() {

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        return ""
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        val posts = if (params.key != null) {
            getPosts(params.key!!)
        } else {
            PagedResponse()
        }
        return LoadResult.Page(
            prevKey = null,
            nextKey = posts.after,
            data = posts.data
        )
    }

    private suspend fun getPosts(after: String): PagedResponse<Post> {
        return repository.getPosts(after)
    }

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        repository.updateSort(sort, timeframe)
        this.invalidate()
    }
}

class HomeRepository(
    apiService: RedditAPIService,
    sort: Sort = Sort.Best,
    timeframe: Timeframe? = null
) : PostRepository(apiService, sort, timeframe) {
    override suspend fun getPosts(after: String): PagedResponse<Post> {
        return makeRequest { apiService.getHome(sort = sort, timeframe = timeframe, after = after) }
    }
}

class SavedRepository(
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
            apiService.getSaved(
                after = after,
                user = username,
                sort = sort,
                timeframe = timeframe
            )
        }
    }
}

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

class PostViewModel(private val repository: PostRepository) : ViewModel() {
    private var _listState by mutableStateOf<LazyListState?>(null)

    @Composable
    fun rememberLazyListState(): LazyListState {
        if (_listState == null) {
            _listState = androidx.compose.foundation.lazy.rememberLazyListState()
        }
        return _listState!!
    }

    var data = Pager(
        config = PagingConfig(pageSize = 100),
        initialKey = "",
        pagingSourceFactory = {
            PostsSource(repository).also {
                postsSource = it
            }
        }
    ).flow.cachedIn(viewModelScope)

    private var postsSource: PostsSource? = null


    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        repository.updateSort(sort, timeframe)
        postsSource?.invalidate()
    }

    fun refresh() {
        data = Pager(
            config = PagingConfig(pageSize = 100),
            initialKey = "",
            pagingSourceFactory = {
                PostsSource(repository).also {
                    postsSource = it
                }
            }
        ).flow.cachedIn(viewModelScope)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostViewer(viewModel: PostViewModel) {
    val posts = viewModel.data.collectAsLazyPagingItems()
    val listState = viewModel.rememberLazyListState()
    PullToRefreshBox(
        isRefreshing = posts.loadState.refresh == LoadState.Loading,
        onRefresh = {
            posts.refresh()
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState,
        ) {
            items(count = posts.itemCount) { index ->
                posts[index]?.let { post ->
                    View(post)
                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewer(viewModel: PostViewModel) {
    val posts = viewModel.data.collectAsLazyPagingItems()
    PullToRefreshBox(
        isRefreshing = posts.loadState.refresh == LoadState.Loading,
        onRefresh = {
            posts.refresh()
        },
    ) {
        PostViewer(viewModel)
    }
}
