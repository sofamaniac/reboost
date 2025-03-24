package com.sofamaniac.reboost

import android.app.admin.TargetUser
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.launch
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
}

class HomeRepository(apiService: RedditAPIService, sort: Sort = Sort.Best, timeframe: Timeframe? = null): PostRepository(apiService, sort, timeframe) {
    override suspend fun getPosts(after: String): PagedResponse<Post> {
        return makeRequest { apiService.getHome(sort = sort, timeframe = timeframe, after = after) }
    }
}

class SavedRepository(apiService: RedditAPIService, sort: Sort = Sort.Best, timeframe: Timeframe? = null): PostRepository(apiService, sort, timeframe) {
    private lateinit var username: String
    override suspend fun getPosts(after: String): PagedResponse<Post> {
        if (!this::username.isInitialized) {
            val response = apiService.getIdentity()
            if (!response.isSuccessful) return PagedResponse()
            username = response.body()!!.username
        }
        return makeRequest { apiService.getSaved(after = after, user = username, sort = sort, timeframe = timeframe) }
    }
}

class PostViewModel(repository: PostRepository) : ViewModel() {
    lateinit var state: LazyListState
    fun isStateInitialized(): Boolean {
        return ::state.isInitialized
    }
    val data = Pager(
        config = PagingConfig(pageSize = 100),
        initialKey = "",
        pagingSourceFactory = {
            PostsSource(repository)
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
fun PostViewer(viewModel: PostViewModel) {
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
        Column {
            Text(text="${posts.itemCount}")
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = viewModel.state,
        ) {
            items(count = posts.itemCount) { index ->
                posts[index]?.let { post ->
                    PostItem(post)
                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                }
            }
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewer(viewModel: PostViewModel) {
    val posts = viewModel.data.collectAsLazyPagingItems()
    val user = viewModel.user
    Column {
        Text("Welcome ${user?.username}")
        PullToRefreshBox(
            isRefreshing = posts.loadState.refresh == LoadState.Loading,
            onRefresh = {
                posts.refresh()
            },
        ) {
            PostViewer(viewModel)
        }
    }
}
