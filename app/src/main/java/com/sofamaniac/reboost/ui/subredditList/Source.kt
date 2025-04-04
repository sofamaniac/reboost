package com.sofamaniac.reboost.ui.subredditList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sofamaniac.reboost.reddit.Subreddit
import com.sofamaniac.reboost.ui.post.PagedResponse

class Source(
    private val repository: Repository
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
