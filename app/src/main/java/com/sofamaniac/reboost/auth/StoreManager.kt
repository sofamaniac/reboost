package com.sofamaniac.reboost.auth

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import net.openid.appauth.AuthState
import androidx.core.content.edit
import com.sofamaniac.reboost.BuildConfig
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenResponse


class StoreManager(context: Context) {
    private val TAG = "AuthManager"
    private val STORE_NAME = "AuthState"
    private val STORE_KEY = "state"

    private  var sharedPreferences: SharedPreferences = context.getSharedPreferences(STORE_NAME, Activity.MODE_PRIVATE)
    private lateinit var authState: AuthState

    var loggedIn: Boolean = false
        private set

    private fun readState() {
        val state = sharedPreferences.getString(STORE_KEY, null)
        if (state != null) {
            authState = AuthState.jsonDeserialize(state)
            loggedIn = true
        } else {
            authState = AuthState()
            loggedIn = false
        }
    }

    private fun writeState() {
        sharedPreferences.edit() {
            putString(STORE_KEY, authState.jsonSerializeString())
        }
    }

    fun getCurrent(): AuthState {
        if (!::authState.isInitialized) {
            readState()
        }
        return authState
    }

    fun update(response: AuthorizationResponse?, exception: AuthorizationException?) {
        getCurrent().update(response, exception)
        Log.d(TAG, "update: $authState")
        loggedIn = true
        writeState()
    }

    fun update(response: TokenResponse?, exception: AuthorizationException?) {
        getCurrent().update(response, exception)
        loggedIn = true
        writeState()
    }

    fun update() {
        loggedIn = true
        writeState()
    }
}