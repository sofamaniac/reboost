package com.sofamaniac.reboost

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import kotlinx.coroutines.launch
import retrofit2.Response

abstract class Paginator<Type : Thing<Any>>(context: Context) : ViewModel() {
    protected val apiService = RedditAPI.getApiService(context)
    protected lateinit var username: String
    private val _posts = mutableStateListOf<Type>()
    val posts: List<Type> get() = _posts
    var after: String? = null
    var count: Int = 0
    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    init {
        firstPage()
    }

    abstract suspend fun requestPage(): Response<Listing<Type>>

    suspend fun handlePage() {
        _isLoading.value = true
        val response = requestPage()
        if (response.isSuccessful) {
            val listing = response.body()
            listing?.let {
                _posts.addAll(it.data.children)
                count += it.size()
                after = it.after()
            }
        }
        _isLoading.value = false

    }

    fun firstPage() {
        viewModelScope.launch {
            if (!::username.isInitialized) {
                username = apiService.getIdentity().body()?.username!!
            }
            handlePage()
        }
    }

    suspend fun loadPage() {
        if (after != null) {
            handlePage()
        }
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

    @Composable
    override fun View(modifier: Modifier) {
        val posts: List<Post> = posts.toList()
        val listState = rememberLazyListState()
        val currentPost: MutableState<Post?> = remember { mutableStateOf(null) }
        if (currentPost.value == null) {
            LazyColumn(
                state = listState,
                modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(posts) { post ->
                    PostItem(post, currentPost = currentPost)
                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                }
                // Use a LazyList item to trigger pagination
                item {
                    PaginationTrigger(listState, posts.size)
                }
                if (isLoading) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        } else {
            OnePostViewer(currentPost.value!!)
        }

    }
}


