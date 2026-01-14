/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.PostDataMapper
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.model.PostData
import retrofit2.Response

interface FeedRepository {
    suspend fun getPosts(after: String, sort: Sort, timeframe: Timeframe? = null): PagedResponse<Thing.Post>
}

abstract class PostRepository(
    val api: RedditAPIService
) : FeedRepository {
    protected suspend fun makeRequest(
        request: suspend () -> Response<Thing.Listing<Thing.Post>>
    ): PagedResponse<Thing.Post> {
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
}

class PostsSource(
    private val repository: PostRepository
) : PagingSource<String, PostData>() {

    private var sort: Sort = Sort.Best
    private var timeframe: Timeframe? = null

    override fun getRefreshKey(state: PagingState<String, PostData>): String {
        return ""
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, PostData> {
        val posts = if (params.key != null) {
            getPosts(params.key!!)
        } else {
            PagedResponse()
        }
        return LoadResult.Page(
            prevKey = null,
            nextKey = posts.after,
            data = posts.data.map {
               PostDataMapper.map(it.data)
            }
        )
    }

    fun setSort(sort: Sort, timeframe: Timeframe? = null) {
        this.sort = sort
        this.timeframe = timeframe
    }
    private suspend fun getPosts(after: String, sort: Sort = Sort.Best, timeframe: Timeframe? = null): PagedResponse<Thing.Post> {
        return repository.getPosts(after, sort, timeframe)
    }

}