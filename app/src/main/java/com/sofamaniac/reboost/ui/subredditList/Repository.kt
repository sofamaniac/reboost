package com.sofamaniac.reboost.ui.subredditList

import android.util.Log
import com.sofamaniac.reboost.reddit.Identity
import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.RedditAPIService
import com.sofamaniac.reboost.reddit.Subreddit
import com.sofamaniac.reboost.ui.post.PagedResponse
import retrofit2.Response

class Repository(
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

    suspend fun getSubreddits(after: String): PagedResponse<Subreddit> {
        return makeRequest { apiService.getSubreddits(after = after) }
    }

    suspend fun getUser(): Identity? {
        return apiService.getIdentity().body()
    }
}
