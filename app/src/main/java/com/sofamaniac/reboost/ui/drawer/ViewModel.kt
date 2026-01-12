/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:46 PM
 *
 */

package com.sofamaniac.reboost.ui.drawer

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.reboost.BuildConfig
import com.sofamaniac.reboost.accounts.AccountsRepository
import com.sofamaniac.reboost.accounts.AccountsRepositoryImpl
import com.sofamaniac.reboost.accounts.RedditAccount
import com.sofamaniac.reboost.auth.AuthConfig
import com.sofamaniac.reboost.auth.BasicAuthClient
import com.sofamaniac.reboost.auth.Scopes
import com.sofamaniac.reboost.auth.createAuthorizationRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

class DrawerViewModel(application: Application) : AndroidViewModel(application) {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private var authService: AuthorizationService = AuthorizationService(application)
    private var accountsRepository: AccountsRepository = AccountsRepositoryImpl(application)

    val accountsList = accountsRepository.accounts
    val activeAccount = accountsRepository.activeAccount

    val serviceConfig = AuthConfig(
        authorizationEndpoint = "https://www.reddit.com/api/v1/authorize",
        tokenEndpoint = "https://www.reddit.com/api/v1/access_token",
        redirectUri = "com.sofamaniac.crabir://callback",
        clientId = BuildConfig.REDDIT_CLIENT_ID,
        /** Add all available scopes */
        scopes = enumValues<Scopes>().map { it.name.lowercase() }
    )

    fun setActiveAccount(accountId: Int) {
        viewModelScope.launch {
            accountsRepository.setActiveAccount(accountId)
        }
    }

    fun createAuthIntent(): Intent {
        val authRequest = createAuthorizationRequest(serviceConfig)
        return authService.getAuthorizationRequestIntent(authRequest)
    }

    fun handleAuthResult(data: Intent?) {
        if (data == null) {
            _loginState.value = LoginState.Error("Login cancelled")
            return
        }

        val authResponse = AuthorizationResponse.fromIntent(data)
        val authException = AuthorizationException.fromIntent(data)

        when {
            authException != null -> {
                Log.e("LoginViewModel", "Authorization exception: $authException")
                _loginState.value = LoginState.Error(
                    authException.message ?: "Authorization exception: $authException"
                )
            }

            authResponse != null -> {
                _loginState.value = LoginState.Loading
                exchangeAuthCodeForToken(authResponse)
            }
        }
    }

    private fun exchangeAuthCodeForToken(authResponse: AuthorizationResponse) {
        val clientAuth = BasicAuthClient(BuildConfig.REDDIT_CLIENT_ID)
        authService.performTokenRequest(
            authResponse.createTokenExchangeRequest(),
            clientAuth
        ) { tokenResponse, ex ->
            when {
                ex != null -> {
                    Log.e("LoginViewModel", "Token exchange failed: $ex")
                    _loginState.value = LoginState.Error("Failed to get access token.")
                }

                tokenResponse != null -> {
                    val authState = AuthState(authResponse, tokenResponse, null)
                    save(authState)
                }

            }
        }
    }

    private fun save(authState: AuthState) {
        viewModelScope.launch {
            try {
                val username = fetchUsername(authState.accessToken!!)
                val accounts = accountsRepository.accounts.first()
                accountsRepository.addAccount(RedditAccount(accounts.size, username, "", authState))
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Failed to save account: $e")
                _loginState.value = LoginState.Error("Failed to save account.")
            }
        }
    }
}

private suspend fun fetchUsername(accessToken: String): String {
    // GET https://oauth.reddit.com/api/v1/me
    // Use OkHttp directly or a lightweight Retrofit service
    return "sofamaniacnsfw" // optional next step
}
