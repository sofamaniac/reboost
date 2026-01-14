/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.api

import com.sofamaniac.reboost.data.remote.dto.post.PostFullname
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface PostAPI {
    /**
     * Votes on a post.
     *
     * Allows a user to cast a vote on a specific post.
     *
     * @param postFullname The full name (ID) of the post to vote on.
     *                 This uniquely identifies the post within the system.
     * @param dir The direction of the vote.
     *                  -  `1`: Upvote
     *                  - `-1`: Downvote
     *                  -  `0`: Neutral/Clear Vote (removes any existing vote)
     * @throws HttpException with a status code of 400 if the vote could not be processed.
     *                       This can occur if the post is too old for example.
     * @throws Throwable if any other error occurs during the request.
     *
     * See [POST /api/vote](https://www.reddit.com/dev/api#POST_api_vote) for more information.
     */
    @POST("/api/vote")
    suspend fun vote(@Query("id") postFullname: PostFullname, @Query("dir") dir: Int)

    @POST("/api/save")
    suspend fun save(@Query("id") postFullname: PostFullname): Response<Unit>

    @POST("/api/unsave")
    suspend fun unsave(@Query("id") postFullname: PostFullname): Response<Unit>

    @POST("/api/hide")
    suspend fun hide(@Query("id") postFullname: PostFullname): Response<Unit>

    @POST("/api/unhide")
    suspend fun unhide(@Query("id") postFullname: PostFullname): Response<Unit>
}
