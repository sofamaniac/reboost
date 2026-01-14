/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 4:45 PM
 *
 */

package com.sofamaniac.reboost.data.repository

import android.content.Context
import android.util.Log
import androidx.compose.ui.util.fastFirstOrNull
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.sofamaniac.reboost.domain.model.RedditAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import net.openid.appauth.AuthState
import java.io.InputStream
import java.io.OutputStream
import java.util.Collections.emptyList

@Serializable
data class Accounts(
    val accounts: List<RedditAccount>,
    val activeId: Int,
)


val Context.dataStore: DataStore<Accounts> by dataStore(
    fileName = "accounts.json",
    AccountsSerializer
)

object AccountsSerializer : Serializer<Accounts> {
    override val defaultValue: Accounts
        get() = Accounts(listOf(RedditAccount.anonymous()), -1)

    override suspend fun readFrom(input: InputStream): Accounts {
        try {
            return Json.decodeFromString<Accounts>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Settings", serialization)
        }
    }

    override suspend fun writeTo(
        t: Accounts,
        output: OutputStream
    ) {
        output.write(
            Json.encodeToString(t)
                .encodeToByteArray()
        )
    }

}

class AccountsRepositoryImpl(
    context: Context,
) : AccountsRepository {
    private val dataStore: DataStore<Accounts> = context.dataStore
    override val accounts: Flow<List<RedditAccount>> = dataStore.data.map {
        Log.d("AccountsRepositoryImpl", "accounts: ${it.accounts}")
        it.accounts
    }
    override val activeAccount: Flow<RedditAccount> = dataStore.data.map {
        if (it.activeId >= it.accounts.size || it.activeId < 0) {
            Log.d("AccountsRepositoryImpl", "Invalid id: activeId: ${it.activeId}")
            RedditAccount.anonymous()
        } else {
            Log.d("AccountsRepositoryImpl", "activeId: ${it.activeId}")
            it.accounts[it.activeId]
        }
    }
    override val activeAccountId: Flow<Int>
        get() = dataStore.data.map { accounts ->
            Log.d("AccountsRepositoryImpl", "activeId: ${accounts.activeId}")
            accounts.activeId
        }

    override suspend fun addAccount(account: RedditAccount) {
        Log.d("AccountsRepositoryImpl", "addAccount: $account")
        dataStore.updateData { accounts ->
            accounts.copy(accounts = accounts.accounts + account)
        }
    }

    override suspend fun setActiveAccount(accountId: Int) {
        dataStore.updateData { accounts ->
            accounts.copy(activeId = accountId)
        }
    }

    override suspend fun deleteAccount(accountId: Int) {
        dataStore.updateData { accounts ->
            accounts.copy(accounts = accounts.accounts.filter { it.id != accountId })
        }
    }

    override suspend fun updateAuthState(accountId: Int, authState: AuthState) {
        dataStore.updateData { accounts ->
            var account = accounts.accounts.fastFirstOrNull { it.id == accountId }
            if (account != null) {
                val accountIndex = accounts.accounts.indexOf(account)
                account = account.copy(auth = authState)
                val accountsList = accounts.accounts.toMutableList()
                accountsList[accountIndex] = account
                accounts.copy(accounts = accountsList)
            } else {
                accounts
            }
        }

    }

//    override suspend fun refreshToken(accountId: Int) {
//        dataStore.updateData { accounts ->
//            var account = accounts.accounts[accountId]
//            // TODO: handle errors
//            val token = tokenRefresher.refreshToken(account.refreshToken)!!
//            account =
//                account.copy(accessToken = token.accessToken!!, refreshToken = token.refreshToken!!)
//            val accountsList = accounts.accounts.toMutableList()
//            accountsList[accountId] = account
//            accounts.copy(accounts = accountsList)
//        }
//
//    }

    override suspend fun clearAll() {
        dataStore.updateData {
            Accounts(emptyList(), -1)
        }
    }
}

