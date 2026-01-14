package com.sofamaniac.reboost.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sofamaniac.reboost.data.remote.api.RedditAPI
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.api.auth.RedditAuthenticator
import com.sofamaniac.reboost.data.remote.interceptors.ForceJsonInterceptor
import com.sofamaniac.reboost.data.remote.interceptors.NetworkInterceptor
import com.sofamaniac.reboost.data.remote.interceptors.RateLimitInterceptor
import com.sofamaniac.reboost.data.remote.interceptors.loggingInterceptor
import com.sofamaniac.reboost.data.remote.utils.URISerializer
import com.sofamaniac.reboost.data.remote.utils.URLSerializer
import com.sofamaniac.reboost.data.repository.AccountsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.URI
import java.net.URL
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://oauth.reddit.com/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRedditAuthenticator(
        accountsRepository: AccountsRepository,
        authService: AuthorizationService,
        clientAuth: ClientAuthentication,
    ): RedditAuthenticator {
        return RedditAuthenticator(accountsRepository, authService, clientAuth)
    }

    @Provides
    @Singleton
    fun provideRateLimiter(): RateLimitInterceptor {
       return RateLimitInterceptor()
    }

    @Provides
    @Singleton
    fun provideForceJsonInterceptor() : ForceJsonInterceptor {
        return ForceJsonInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: RedditAuthenticator,
        rateLimitInterceptor: RateLimitInterceptor,
        forceJsonInterceptor: ForceJsonInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(rateLimitInterceptor)
            .addInterceptor(forceJsonInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    fun provideJson() : Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
            serializersModule = SerializersModule {
                contextual(URL::class, URLSerializer)
                contextual(URI::class, URISerializer)
            }
        }
    }

    @Provides
    @Singleton
    fun provideRedditApiService(
        okHttpClient: OkHttpClient,
        json: Json
    ): RedditAPIService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(RedditAPIService::class.java)
    }
}