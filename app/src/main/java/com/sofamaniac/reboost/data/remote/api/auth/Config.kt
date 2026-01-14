/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.api.auth

import android.util.Base64
import androidx.core.net.toUri
import com.sofamaniac.reboost.BuildConfig
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ResponseTypeValues

class AuthConfig(
) {
    val authorizationEndpoint = "https://www.reddit.com/api/v1/authorize"
    private val tokenEndpoint = "https://www.reddit.com/api/v1/access_token"
    private val redirectUri = "com.sofamaniac.crabir://callback"
    private val clientId = BuildConfig.REDDIT_CLIENT_ID

    /** Add all available scopes */
    private val scopes = enumValues<Scopes>().map { it.name.lowercase() }

    fun createAuthorizationRequest(): AuthorizationRequest {
        val serviceConfiguration = AuthorizationServiceConfiguration(
            authorizationEndpoint.toUri(),
            tokenEndpoint.toUri()
        )

        return AuthorizationRequest.Builder(
            serviceConfiguration,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri.toUri(),
        )
            .setScopes(*scopes.toTypedArray())
            .setAdditionalParameters(mapOf<String?, String?>("duration" to "permanent"))
            .build()
    }
}


class BasicAuthClient(private val clientId: String) : ClientAuthentication {
    override fun getRequestHeaders(clientId: String): MutableMap<String, String> {
        return mutableMapOf(
            "Authorization" to "Basic " + Base64.encodeToString("$clientId:".toByteArray(), Base64.NO_WRAP)
        )
    }

    override fun getRequestParameters(clientId: String): Map<String?, String?> {
        return mutableMapOf()
    }
}

