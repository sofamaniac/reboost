/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.subredditList

import android.util.Log
import com.sofamaniac.reboost.reddit.Identity
import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.PagedResponse
import com.sofamaniac.reboost.reddit.RedditAPIService
import com.sofamaniac.reboost.reddit.Subreddit
import retrofit2.Response

class Repository(
    private val apiService: RedditAPIService,
) {
    private suspend fun makeRequest(
        request: suspend () -> Response<Listing<Subreddit>>
    ): PagedResponse<Subreddit> {
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

    suspend fun getSubreddits(after: String): PagedResponse<Subreddit> {
        return makeRequest { apiService.getSubreddits(after = after) }
    }

    suspend fun getUser(): Identity? {
        return apiService.getIdentity().body()
    }
}
