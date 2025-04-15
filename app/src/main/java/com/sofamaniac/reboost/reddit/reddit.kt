package com.sofamaniac.reboost.reddit

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sofamaniac.reboost.BuildConfig
import com.sofamaniac.reboost.auth.BasicAuthClient
import com.sofamaniac.reboost.auth.StoreManager
import com.sofamaniac.reboost.reddit.comment.CommentAPI
import com.sofamaniac.reboost.reddit.post.PostAPI
import com.sofamaniac.reboost.reddit.post.PostId
import com.sofamaniac.reboost.reddit.subreddit.SubredditName
import com.sofamaniac.reboost.reddit.utils.CommentsResponseSerializer
import com.sofamaniac.reboost.reddit.utils.URISerializer
import com.sofamaniac.reboost.reddit.utils.URLSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.URI
import java.net.URL
import com.sofamaniac.reboost.reddit.comment.Sort as CommentSort
import com.sofamaniac.reboost.reddit.post.Sort as PostSort
import com.sofamaniac.reboost.reddit.post.Timeframe as PostTimeframe

private const val BASE_URL = "https://oauth.reddit.com/"


val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}


private const val API_LIMIT = 100

interface RedditAPIService : CommentAPI, PostAPI {

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

    @GET("/r/{subreddit}/about")
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
    suspend fun getPostFromPermalink(
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
    /** List of [Comment] with an optional [More] at the very end */
    val comments: Listing<Thing>,
    val more: More? = null,
)

@Serializable
data class Identity(
    @SerialName("name") val username: String = "",
)

object RedditAPI {
    lateinit var service: RedditAPIService
    fun init(context: Context) {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
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
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(RateLimitInterceptor())
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ForceJsonInterceptor())
            .addInterceptor(NetworkInterceptor())
            .build()
    }
}

class AuthInterceptor(context: Context) : Interceptor {
    private val authManager = StoreManager(context)
    private val authService = AuthorizationService(context)
    private val clientAuth: ClientAuthentication = BasicAuthClient(BuildConfig.REDDIT_CLIENT_ID)


    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()
        Log.d("AuthInterceptor", "intercept")

        if (!authManager.authState.needsTokenRefresh) {
            Log.d("AuthInterceptor", "Refreshing not needed")
            requestBuilder.addHeader(
                "Authorization",
                "Bearer ${authManager.authState.accessToken}"
            )
        } else {
            authManager.authState.performActionWithFreshTokens(
                authService,
                clientAuth
            ) { accessToken, _, ex ->
                if (ex != null) {
                    Log.e("AuthInterceptor", "Token refresh failed: $ex")
                } else {
                    Log.d("AuthInterceptor", "Token refreshed successfully")
                    authManager.update()
                    requestBuilder.addHeader("Authorization", "Bearer $accessToken")
                }
                Log.e("AuthInterceptor", "Token needs refresh")
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}

class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Log the response body here for debugging
        val responseBody = response.peekBody(Long.MAX_VALUE).string()
        println("Response Body: $responseBody")

        return response
    }
}

class RateLimitInterceptor : Interceptor {

    private var lastRequestTime: Long = 0
    private var resetTime: Long = 0
    private var remaining: Long = 0
    private val minTimeBetweenRequests = 1000L // 1 second

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val currentTime = System.currentTimeMillis()
        val timeSinceLastRequest = currentTime - lastRequestTime

        if (remaining <= 0) {
            if (currentTime < resetTime) {
                val delay = resetTime - currentTime
                Log.w(
                    "RateLimitInterceptor",
                    "Rate limit reached, waiting for ${delay}ms (used all requests)"
                )

                Thread.sleep(delay)
            }
        } else if (timeSinceLastRequest < minTimeBetweenRequests) {
            val delay = minTimeBetweenRequests - timeSinceLastRequest
            Log.w(
                "RateLimitInterceptor",
                "Rate limit reached, waiting for ${delay}ms (${remaining} requests remaining)"
            )
            Thread.sleep(delay)
        }

        lastRequestTime = System.currentTimeMillis()
        val request = chain.request()
        val response = chain.proceed(request)

        Log.d("RateLimitInterceptor", "Response headers: ${response.headers}")

        remaining = (response.header("x-ratelimit-remaining")?.toFloatOrNull() ?: 0f).toLong()
        resetTime = response.header("x-ratelimit-reset")?.toLongOrNull()?.let {
            System.currentTimeMillis() + it * 1000 // Convert seconds to milliseconds
        } ?: 0

        return response
    }
}

/**
 * From the top of [Reddit API wiki](https://www.reddit.com/dev/api/)
 * For legacy reasons, all JSON response bodies currently have <, >, and &
 * replaced with &lt;, &gt;, and &amp;, respectively.
 * If you wish to opt out of this behaviour, add a raw_json=1 parameter to your request.
 */
class ForceJsonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        // TODO: not sure if needed
        val headerRequest = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .build()
        val url = headerRequest.url.newBuilder()
            .addQueryParameter("raw_json", "1").build()
        val request = headerRequest.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}