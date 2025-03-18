package com.sofamaniac.reboost

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.RedditAPIService
import com.sofamaniac.reboost.reddit.SimpleSort
import com.sofamaniac.reboost.reddit.Thing
import kotlinx.coroutines.launch
import retrofit2.Response

data class PagedResponse<T : Thing<Any>>(
    val data: List<T> = emptyList<T>(),
    val after: String? = "",
    val total: Int = 0
)

class PostRepository(
    private val apiService: RedditAPIService
) {
    private suspend fun makeRequest(
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

    suspend fun getHot(after: String): PagedResponse<Post> {
        return makeRequest { apiService.getSorted(sort = SimpleSort.Hot, after=after) }
    }

    suspend fun getBest(after: String): PagedResponse<Post> {
        return makeRequest { apiService.getSorted(sort = SimpleSort.Best, after) }
    }
}

class HomePostsSource(
    private val repository: PostRepository
) : PagingSource<String, Post>() {

    var isLoading = false;

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        val posts = if (params.key != null) {
            repository.getHot(params.key!!)
        } else {
            PagedResponse()
        }
        return LoadResult.Page(
            prevKey = null,
            nextKey = posts.after,
            data = posts.data
        )
    }
}

class HomeViewModel(val repository: PostRepository) : ViewModel() {
    var factory = HomePostsSource(repository)
    var isRefreshing = false
    val data = Pager(
        config = PagingConfig(pageSize = 100),
        initialKey = "",
        pagingSourceFactory = {newFactory()}
    ).flow.cachedIn(viewModelScope)

    fun newFactory(): HomePostsSource {
        factory = HomePostsSource(repository)
        return factory
    }

    fun refresh() {
        isRefreshing = true
        factory.invalidate()
        isRefreshing = false
    }

    fun isLoading() : Boolean {
        return isRefreshing
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewer(viewModel: HomeViewModel) {
    val posts = viewModel.data.collectAsLazyPagingItems()
    PullToRefreshBox(
        isRefreshing = viewModel.isLoading(),
        onRefresh = { viewModel.refresh()}
    ) {
    LazyColumn {
        items(count = posts.itemCount) { index ->
            posts[index]?.let { post ->
                PostItem(post)
                HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
            }
        }
    }
    }
}

abstract class Paginator<Type : Thing<Any>>(context: Context) : ViewModel() {
    protected val apiService = RedditAPI.getApiService(context)
    private lateinit var _username: String
    protected val username: String
        get() {
            if (!::_username.isInitialized) {
                viewModelScope.launch {
                    val response = apiService.getIdentity()
                    if (response.isSuccessful) {
                        _username = response.body()!!.username
                    } else {
                        Log.e("Paginator", "Failed to get username: ${response.errorBody()}")
                    }
                }
            }
            if (!::_username.isInitialized) {
                return "Anonymous"
            } else {
                return _username
            }
        }
    private val _posts = mutableStateListOf<Type>()
    val posts: List<Type> get() = _posts
    var after: String? = ""
    var count: Int = 0
    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    abstract suspend fun requestPage(): Response<Listing<Type>>

    suspend fun handlePage() {
        _isLoading.value = true
        val response = requestPage()
        if (response.isSuccessful) {
            Log.d("handlePage", "code ${response.code()}")
            val listing = response.body()
            listing?.let {
                _posts.addAll(it.data.children)
                count += it.size()
                after = it.after()
            }
        }
        _isLoading.value = false
    }

    suspend fun loadPage() {
        if (after != null) {
            handlePage()
        }
    }

    fun refresh() {
        _posts.clear()
        after = ""
        count = 0
    }

    @Composable
    abstract fun View(modifier: Modifier = Modifier)

    @Composable
    fun PaginationTrigger(listState: LazyListState, listSize: Int) {
        val shouldLoadMore by remember {
            derivedStateOf {
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index != null && lastVisibleItem.index >= listSize - 2
            }
        }

        LaunchedEffect(shouldLoadMore) {
            if (shouldLoadMore) {
                Log.d("PostViewer", "Loading page (end of list)")
                loadPage()
            }
        }
    }
}

abstract class PostViewer(context: Context) : Paginator<Post>(context) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun View(modifier: Modifier) {
        val posts: List<Post> = posts.toList()
        val listState = rememberLazyListState()
        val currentPost: MutableState<Post?> = remember { mutableStateOf(null) }
        if (currentPost.value == null) {
            PullToRefreshBox(
                isRefreshing = isLoading,
                onRefresh = {
                    refresh()
                }
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(posts) { post ->
                        PostItem(post)
                        HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                    }
                    // Use a LazyList item to trigger pagination
                    item {
                        PaginationTrigger(listState, posts.size)
                    }
                }
            }
        } else {
            OnePostViewer(currentPost.value!!)
        }

    }
}


