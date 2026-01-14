package com.sofamaniac.reboost.data.remote.api.auth

import net.openid.appauth.TokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RedditAuthApi {

    @FormUrlEncoded
    @POST("access_token")
    fun refreshToken(
        @Header("Authorization") basicAuth: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): Call<TokenResponse>
}