/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.auth

import com.sofamaniac.reboost.BuildConfig
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService


/** Handles everything related to the authentication process.
 * Before dropping the object, call [dispose] to release resources. */
class Manager(context: android.content.Context) {
    var authService: AuthorizationService private set
    var authManager: StoreManager private set
    var authRequest: AuthorizationRequest private set
    val authConfig = AuthConfig(
        authorizationEndpoint = "https://www.reddit.com/api/v1/authorize",
        tokenEndpoint = "https://www.reddit.com/api/v1/access_token",
        redirectUri = "com.sofamaniac.crabir://callback",
        clientId = BuildConfig.REDDIT_CLIENT_ID,
        /** Add all available scopes */
        scopes = enumValues<Scopes>().map { it.name.lowercase() }
    )
    init {
        authService = AuthorizationService(context)
        authManager = StoreManager(context)
        authRequest = createAuthorizationRequest(authConfig)
    }

    fun dispose() {
        authService.dispose()
    }
}
