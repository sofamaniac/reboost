package com.sofamaniac.reboost.reddit.comment

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sofamaniac.reboost.reddit.Comment
import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.PagedResponse
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.Sort
import com.sofamaniac.reboost.reddit.Thing
import com.sofamaniac.reboost.reddit.Timeframe
import retrofit2.Response

abstract class CommentRepository(
    val post: Post,
    var sort: Sort = Sort.Best,
    var timeframe: Timeframe? = null
) {


    protected suspend fun makeRequest(
        request: suspend () -> Response<Array<Listing<Thing>>>
    ): PagedResponse<Comment> {
        val response = request()
        if (response.isSuccessful) {
            Log.d("makeRequest", "code ${response.code()}")
            val listing = response.body()?.get(1) as Listing<Comment>?
            listing?.let {
                return PagedResponse(
                    data = it.data.children,
                    after = it.data.after,
                    total = it.size()
                )
            }
            return PagedResponse()
        }
        Log.e("makeRequest", "Error making request : ${response.errorBody()}")
        return PagedResponse()
    }

    open suspend fun getComments(after: String): PagedResponse<Comment> {
        return makeRequest {
            RedditAPI.service.getComments(
                subreddit = post.data.subreddit,
                id = post.data.id,
                after = after
            )
        }
    }

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        this.sort = sort
        this.timeframe = timeframe
    }

}

class CommentsSource(
    private val repository: CommentRepository
) : PagingSource<String, Comment>() {

    override fun getRefreshKey(state: PagingState<String, Comment>): String? {
        return ""
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Comment> {
        val comments = if (params.key != null) {
            getComments(params.key!!)
        } else {
            PagedResponse()
        }
        return LoadResult.Page(
            prevKey = null,
            nextKey = comments.after,
            data = comments.data
        )
    }

    private suspend fun getComments(after: String): PagedResponse<Comment> {
        return repository.getComments(after)
    }

}
