package com.sofamaniac.reboost.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * From the top of [Reddit API wiki](https://www.reddit.com/dev/api/)
 * For legacy reasons, all JSON response bodies currently have <, >, and &
 * replaced with &lt;, &gt;, and &amp;, respectively.
 * If you wish to opt out of this behaviour, add a raw_json=1 parameter to your request.
 */
class ForceJsonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
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