/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:52 PM
 *
 */

package com.sofamaniac.reboost.data.remote.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sofamaniac.reboost.BuildConfig
import com.sofamaniac.reboost.data.repository.AccountsRepositoryImpl
import com.sofamaniac.reboost.data.remote.api.auth.BasicAuthClient
import com.sofamaniac.reboost.data.remote.api.auth.RedditAuthApi
import com.sofamaniac.reboost.data.remote.api.auth.RedditAuthenticator
import com.sofamaniac.reboost.data.remote.dto.Thing.Listing
import com.sofamaniac.reboost.data.remote.dto.Thing.More
import com.sofamaniac.reboost.data.remote.dto.Thing.Post
import com.sofamaniac.reboost.data.remote.dto.Thing.Subreddit
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.post.PostId
import com.sofamaniac.reboost.data.remote.interceptors.ForceJsonInterceptor
import com.sofamaniac.reboost.data.remote.interceptors.NetworkInterceptor
import com.sofamaniac.reboost.data.remote.interceptors.RateLimitInterceptor
import com.sofamaniac.reboost.data.remote.interceptors.loggingInterceptor
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditName
import com.sofamaniac.reboost.data.remote.utils.CommentsResponseSerializer
import com.sofamaniac.reboost.data.remote.utils.URISerializer
import com.sofamaniac.reboost.data.remote.utils.URLSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import net.openid.appauth.AuthorizationService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.URI
import java.net.URL
import com.sofamaniac.reboost.data.remote.dto.comment.Sort as CommentSort
import com.sofamaniac.reboost.data.remote.dto.post.Sort as PostSort
import com.sofamaniac.reboost.data.remote.dto.Timeframe as PostTimeframe

private const val BASE_URL = "https://oauth.reddit.com/"


private const val API_LIMIT = 100

interface RedditAPIService : CommentAPI, PostAPI, RedditAuthApi {

    @GET("api/v1/me.json")
    suspend fun getIdentity(): Response<Identity>

    @GET("user/{user}/saved.json")
    suspend fun getSaved(
        @Path("user") user: String,
        @Query("sort") sort: PostSort = PostSort.New,
        @Query("t") timeframe: PostTimeframe? = null,
        @Query("after") after: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
    ): Response<Listing<Post>>

    @GET("{sort}.json")
    suspend fun getHome(
        @Path("sort") sort: PostSort,
        @Query("t") timeframe: PostTimeframe? = null,
        @Query("after") after: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Post>>

    @GET("/r/{subreddit}/about.json")
    suspend fun getSubInfo(@Path("subreddit") subreddit: SubredditName): Response<Subreddit>

    @GET("user/{username}/about.json")
    suspend fun getUserAbout(@Path("username") username: String): Response<Listing<Post>>

    @GET("/subreddits/mine/subscriber")
    suspend fun getSubreddits(
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT
    ): Response<Listing<Subreddit>>

    @GET("/r/{subreddit}/{sort}.json")
    suspend fun getSubreddit(
        @Path("subreddit") subreddit: SubredditName,
        @Path("sort") sort: PostSort = PostSort.Best,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("t") timeframe: PostTimeframe? = null,
        @Query("sr_detail") srDetail: Boolean = true,
    ): Response<Listing<Post>>

    /**
     * Gets the comments for a post.
     *
     * @param subreddit The name of the subreddit where the post is located.
     * @param id The ID of the post.
     * @param comment focal point of the returned view
     * @param context Number of parents to be shown when [comment] is set
     * @param depth is the maximum depth of subtrees in the thread
     * @param showMore whether to show [More] or not
     */
    @GET("/r/{subreddit}/comments/{id}.json")
    suspend fun getComments(
        @Path("subreddit") subreddit: SubredditName,
        @Path("id") id: PostId,
        @Query("showedits") showEdits: Boolean = true,
        @Query("showmore") showMore: Boolean = true,
        @Query("showmedia") showMedia: Boolean = true,
        @Query("showtitle") showTitle: Boolean = true,
        @Query("sort") sort: CommentSort? = null,
        @Query("comment") comment: String? = null,
        @Query("context") context: Int? = null,
        @Query("depth") depth: Int? = null,
        @Query("limit") limit: Int? = null,
    ): Response<CommentsResponse>

    @GET("{permalink}.json")
    suspend fun getThread(
        @Path(value = "permalink", encoded = true) permalink: String,
        @Query("showedits") showEdits: Boolean = false,
        @Query("showmore") showMore: Boolean = false,
        @Query("showmedia") showMedia: Boolean = false,
        @Query("showtitle") showTitle: Boolean = false,
        @Query("sort") sort: CommentSort = CommentSort.Best,
        @Query("comment") comment: String? = null,
        @Query("context") context: Int? = 0,
        @Query("depth") depth: Int? = null,
        @Query("limit") limit: Int = API_LIMIT,
    ): Response<CommentsResponse>
}

@Serializable(with = CommentsResponseSerializer::class)
data class CommentsResponse(
    /** Contains only 1 (one) [Post] */
    val post: Listing<Post>,
    /** List of [com.sofamaniac.reboost.data.remote.dto.Comment] and [More] */
    val comments: Listing<Thing>,
    val more: More? = null,
)

@Serializable
data class Identity(
    @SerialName("name") val username: String = "",
)

class RedditAPI {
    lateinit var service: RedditAPIService
    lateinit var authService: AuthorizationService
    fun init(context: Context) {
        authService = AuthorizationService(context)
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
            serializersModule = SerializersModule {
                contextual(URL::class, URLSerializer)
                contextual(URI::class, URISerializer)
            }
        }


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okhttpClient(context))
            .build()
        service = retrofit.create(RedditAPIService::class.java)

    }

    private fun okhttpClient(context: Context): OkHttpClient {
        val accountsRepository = AccountsRepositoryImpl(context)

        return OkHttpClient.Builder()
            .addInterceptor(
                RedditAuthenticator(
                    accountsRepository,
                    authService,
                    BasicAuthClient(BuildConfig.REDDIT_CLIENT_ID)
                )
            )
            //.addInterceptor(AuthInterceptor(context))
            .addInterceptor(RateLimitInterceptor())
            .addInterceptor(ForceJsonInterceptor())
            .addInterceptor(NetworkInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
