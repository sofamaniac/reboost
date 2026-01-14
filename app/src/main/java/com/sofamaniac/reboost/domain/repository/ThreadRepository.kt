package com.sofamaniac.reboost.domain.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.PostDataMapper
import com.sofamaniac.reboost.data.remote.dto.comment.Sort
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.model.PostData
import retrofit2.Response

interface ThreadRepository {
    suspend fun getComments(
        permalink: String,
        sort: Sort, timeframe: Timeframe? = null): List<Thing>
    suspend fun getPost(): Thing.Post?
    suspend fun getMoreComments(more: Thing.More): List<Thing>
    fun getPostId(permalink: String): String

    fun isRefreshing(): Boolean

    fun refresh()
}

class ThreadRepositoryImpl(val api: RedditAPIService): ThreadRepository {
    private var post: Thing.Post? = null
    private var comments: List<Thing> = emptyList()
    private var isRefreshing = false

    suspend fun fetchThread(permalink: String, sort: Sort) {
        if (post != null && comments.isNotEmpty()) {
            return
        }
        isRefreshing = true
        val response = api.getThread(permalink, sort = sort)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                post = body.post.data.children.firstOrNull()
                comments = body.comments.data.children
            }
        }
        isRefreshing = false
    }

    override suspend fun getPost(): Thing.Post? {
        return post
    }

    override suspend fun getComments(
        permalink: String,
        sort: Sort,
        timeframe: Timeframe?
    ): List<Thing> {
        fetchThread(permalink, sort)
        return comments
    }

    override fun getPostId(permalink: String): String {
        val segments = permalink.split("/")
        val index = segments.indexOf("comments")
        return segments[index + 1]
    }

    override suspend fun getMoreComments(more: Thing.More): List<Thing.Comment> {
        TODO("Not yet implemented")
    }

    override fun refresh() {
        comments = emptyList()
    }

    override fun isRefreshing(): Boolean {
        return isRefreshing
    }
}