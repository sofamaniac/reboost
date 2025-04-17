/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit.post

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.PagedResponse
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import retrofit2.Response

abstract class PostRepository(
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
                    after = it.data.after,
                    total = it.size
                )
            }
            return PagedResponse()
        }
        Log.e("makeRequest", "Error making request : ${response.errorBody()}")
        return PagedResponse()
    }

    open suspend fun getPosts(after: String): PagedResponse<Post> {
        return makeRequest {
            RedditAPI.service.getHome(
                sort = sort,
                timeframe = timeframe,
                after = after
            )
        }
    }

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        this.sort = sort
        this.timeframe = timeframe
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