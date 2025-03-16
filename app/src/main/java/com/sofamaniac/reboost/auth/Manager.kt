package com.sofamaniac.reboost.auth

import android.content.Context
import com.sofamaniac.reboost.BuildConfig
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService


/** Handles everything related to the authentication process.
 * Before dropping the object, call [dispose] to release resources. */
class Manager(context: Context) {
    lateinit var authService: AuthorizationService private set
    lateinit var authManager: StoreManager private set
    lateinit var authRequest: AuthorizationRequest private set
    val authConfig = AuthConfig(
        authorizationEndpoint = "https://ssl.reddit.com/api/v1/authorize",
        tokenEndpoint = "https://ssl.reddit.com/api/v1/access_token",
        redirectUri = "com.sofamaniac.reboost://callback",
        clientId = BuildConfig.REDDIT_CLIENT_ID,
        /** Add all available scopes */
        scopes = enumValues<Scopes>().map { it.name.lowercase() }
    )
    init {
        authService = AuthorizationService(context)
        authManager = StoreManager(context)
        authRequest = createAuthorizationRequest(authConfig)
    }

    public fun dispose() {
        authService.dispose()
    }
}
