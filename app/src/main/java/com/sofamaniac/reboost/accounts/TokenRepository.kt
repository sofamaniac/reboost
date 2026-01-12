/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 3:48 PM
 *
 */

package com.sofamaniac.reboost.accounts

import android.util.Base64
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sofamaniac.reboost.BuildConfig
import com.sofamaniac.reboost.reddit.RedditAuthApi
import com.sofamaniac.reboost.reddit.utils.URISerializer
import com.sofamaniac.reboost.reddit.utils.URLSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import net.openid.appauth.TokenResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.URI
import java.net.URL

interface TokenRefresher {
    fun refreshToken(refreshToken: String): TokenResponse?
}

class TokenRefresherImpl(
) : TokenRefresher {
    private lateinit var authService: RedditAuthApi

    init {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            serializersModule = SerializersModule {
                contextual(URL::class, URLSerializer)
                contextual(URI::class, URISerializer)
            }
        }


        // AuthService as to be set up before RedditAPIService
        val authClient = OkHttpClient.Builder().build();
        val contentType = "application/json".toMediaType()
        val authRetrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com/api/v1/")
            .client(authClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

    }

    private val basicAuthHeader =
        "Basic " + Base64.encodeToString(
            "${BuildConfig.REDDIT_CLIENT_ID}:".toByteArray(),
            Base64.NO_WRAP
        )

    override fun refreshToken(refreshToken: String): TokenResponse? {

        return try {
            val response = authService
                .refreshToken(
                    basicAuth = basicAuthHeader,
                    refreshToken = refreshToken
                )
                .execute()

            if (response.isSuccessful) {
                response.body()
            } else null
        } catch (e: Exception) {
            null
        }
    }
}