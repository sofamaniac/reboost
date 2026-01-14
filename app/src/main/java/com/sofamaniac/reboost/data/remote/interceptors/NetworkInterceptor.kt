package com.sofamaniac.reboost.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Log the response body here for debugging
        //val responseBody = response.peekBody(Long.MAX_VALUE).string()
        //println("Response Body: $responseBody")

        return response
    }
}