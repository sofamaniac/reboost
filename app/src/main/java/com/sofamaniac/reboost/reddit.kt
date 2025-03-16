package com.sofamaniac.reboost

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sofamaniac.reboost.auth.StoreManager
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val BASE_URL = "https://oauth.reddit.com/"


val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}


@Serializable
abstract class Thing<out T>(val kind: String) {
    abstract val data: T
    abstract fun fullname(): String
    abstract fun id(): String
}

@Serializable
data class ListingData<Type>(
    val after: String? = null,
    val dist: Int = 0,
    val modhash: String? = null,
    val children: List<Type> = emptyList<Type>()
)

@Serializable
data class Listing<Type: Thing<Any>>(
    val kind: String = "Listing",
    val data: ListingData<Type> = ListingData()
) : Iterable<Type> {
    override fun iterator(): Iterator<Type> {
        return data.children.iterator()
    }
    fun size(): Int {
        return data.children.size
    }
    fun after(): String? {
        Log.d("Listing", "after: ${data.after}")
        return data.after
    }
}

typealias Post = PostThing
@Serializable
class PostThing(override val data: PostData): Thing<PostData>(kind = "t3") {
    override fun fullname(): String {
        return data.name
    }

    override fun id(): String {
        return data.id
    }
}

@Serializable
data class PostData(
    val approved_at_utc: String? = null,
    val subreddit: String = "",
    val selftext: String = "",
    val author_fullname: String = "",
    val saved: Boolean = false,
    val mod_reason_title: String? = null,
    val gilded: Int = 0,
    val clicked: Boolean = false,
    val title: String = "",
    val name: String = "",
    val id: String = "",
    val url: String = "",
    val preview: PostPreview = PostPreview(),
//    val link_flair_richtext: List<String> = emptyList<String>(),
    val subreddit_name_prefixed: String = "",
    val hidden: Boolean = false,
    val pwls: String? = null,
    val post_hint: String? = null,
)

@Serializable
data class PostPreview(
    val images: List<PostImage> = emptyList()
)

@Serializable
data class PostImage(
    val source: PostImageSource,
    val resolutions: List<PostImageSource>
)

@Serializable
data class PostImageSource(
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0
)

private const val API_LIMIT = 100

interface RedditAPIService {

    @GET("api/v1/me")
    suspend fun getIdentity(): Response<Identity>

    @GET("user/{user}/saved")
    suspend fun getSavedPosts(
        @Path("user") user: String,
        @Query("after") after: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
    ): Response<Listing<Post>>

    @GET("hot.json")
    suspend fun getHotPosts(
        @Query("after") after: String? = null,
        @Query("count") count: Int = 0,
        @Query("limit") limit: Int = API_LIMIT,
    ): Response<Listing<Post>>

    @GET("user/{username}/about")
    suspend fun getUserAbout(@Path("username") username: String): Response<Listing<Post>>
}

@Serializable
data class Identity (
    @SerialName("name") val username: String = "",
)

object RedditAPI {
    private lateinit var apiService: RedditAPIService

    fun getApiService(context: Context): RedditAPIService {
        if (!::apiService.isInitialized) {
            val contentType = "application/json".toMediaType()
            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            }
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(json.asConverterFactory(contentType))
                .client(okhttpClient(context))
                .build()
            apiService = retrofit.create(RedditAPIService::class.java)
        }
        return apiService
    }

    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(RateLimitInterceptor())
            .addInterceptor(loggingInterceptor)
            .addInterceptor(NetworkInterceptor())
            .build()
    }
}

class AuthInterceptor(context: Context) : Interceptor {
    private val authManager = StoreManager(context)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()

        if (!authManager.getCurrent().needsTokenRefresh) {
            requestBuilder.addHeader(
                "Authorization",
                "Bearer ${authManager.getCurrent().accessToken}"
            )
        } else {
            Log.e("AuthInterceptor", "Token needs refresh")
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
                Log.w("RateLimitInterceptor", "Rate limit reached, waiting for ${delay}ms (used all requests)")

                Thread.sleep(delay)
            }
        } else if (timeSinceLastRequest < minTimeBetweenRequests) {
            val delay = minTimeBetweenRequests - timeSinceLastRequest
            Log.w("RateLimitInterceptor", "Rate limit reached, waiting for ${delay}ms (${remaining} requests remaining)")
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