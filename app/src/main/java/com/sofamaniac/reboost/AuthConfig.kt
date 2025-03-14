package com.sofamaniac.reboost

import android.app.Activity
import androidx.core.net.toUri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

data class AuthConfig(
    val authorizationEndpoint: String,
    val tokenEndpoint: String,
    val redirectUri: String,
    val clientId: String,
    val scopes: List<String>
)

fun createAuthorizationRequest(authConfig: AuthConfig): AuthorizationRequest {
    val serviceConfiguration = AuthorizationServiceConfiguration(
        authConfig.authorizationEndpoint.toUri(),
        authConfig.tokenEndpoint.toUri()
    )

    return AuthorizationRequest.Builder(
        serviceConfiguration,
        authConfig.clientId,
        ResponseTypeValues.CODE,
        authConfig.redirectUri.toUri(),
    )
        .setScopes(*authConfig.scopes.toTypedArray())
        .setAdditionalParameters(mapOf<String?, String?>("duration" to "permanent"))
        .build()
}

