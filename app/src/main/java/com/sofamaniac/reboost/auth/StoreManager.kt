/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.auth

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.TokenResponse


class StoreManager(val context: Context) {
    private val TAG = "AuthManager"
    private val STORE_KEY = "state"
    private val STORE_NAME = "auth_state"

    private  var sharedPreferences: SharedPreferences = context.getSharedPreferences(STORE_NAME, Activity.MODE_PRIVATE)
    private lateinit var _authState: AuthState
    val authState: AuthState
        get() {
            if (!::_authState.isInitialized) {
                readState()
            }
            return _authState
        }

    var loggedIn: Boolean = false
        private set

    private fun readState() {
        val state = sharedPreferences.getString(STORE_KEY, null)
        if (state != null) {
            Log.d(TAG, "Found existing data: $state")
            _authState = AuthState.jsonDeserialize(state)
            loggedIn = true
        } else {
            Log.d(TAG, "No prior authentication found")
            _authState = AuthState()
            loggedIn = false
        }
    }

    private fun writeState() {
        sharedPreferences.edit {
            putString(STORE_KEY, _authState.jsonSerializeString())
        }
    }

    fun update(response: AuthorizationResponse?, exception: AuthorizationException?) {
        authState.update(response, exception)
        Log.d(TAG, "update: $_authState")
        loggedIn = true
        writeState()
    }

    fun update(response: TokenResponse?, exception: AuthorizationException?) {
        authState.update(response, exception)
        loggedIn = true
        writeState()
    }

    fun update() {
        loggedIn = true
        writeState()
    }
}


