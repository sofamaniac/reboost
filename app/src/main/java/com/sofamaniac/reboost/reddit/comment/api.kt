package com.sofamaniac.reboost.reddit.comment

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query


interface CommentAPI {
    @POST("/api/vote")
    suspend fun vote(@Query("id") commentFullname: CommentFullname, @Query("dir") dir: Int)

    @POST("/api/save")
    suspend fun save(@Query("id") commentFullname: CommentFullname): Response<Unit>

    @POST("/api/unsave")
    suspend fun unsave(@Query("id") commentFullname: CommentFullname): Response<Unit>
}