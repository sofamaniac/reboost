/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.api

import com.sofamaniac.reboost.data.remote.dto.user.User
import retrofit2.http.GET
import retrofit2.http.Path


interface UserAPI {

    @GET("/user/{username}/about.json")
    suspend fun getUser(@Path("username") username: String): User

}

