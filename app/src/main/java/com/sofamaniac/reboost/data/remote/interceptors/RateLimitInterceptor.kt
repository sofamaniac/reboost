package com.sofamaniac.reboost.data.remote.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RateLimitInterceptor : Interceptor {

    private var lastRequestTime: Long = 0
    private var resetTime: Long = 0
    private var remaining: Long = 0
    private val minTimeBetweenRequests = 1000L // 1 second

    override fun intercept(chain: Interceptor.Chain): Response {
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