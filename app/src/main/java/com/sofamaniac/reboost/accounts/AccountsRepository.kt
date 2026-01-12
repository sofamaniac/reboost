/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 12:42 PM
 *
 */

package com.sofamaniac.reboost.accounts

import kotlinx.coroutines.flow.Flow
import net.openid.appauth.AuthState


interface AccountsRepository {
    val accounts: Flow<List<RedditAccount>>
    val activeAccount: Flow<RedditAccount>
    val activeAccountId: Flow<Int>

    suspend fun addAccount(account: RedditAccount)
    suspend fun setActiveAccount(accountId: Int)
    suspend fun deleteAccount(accountId: Int)

    //suspend fun refreshToken(accountId: Int)
    suspend fun updateAuthState(accountId: Int, authState: AuthState)
    suspend fun clearAll()
}

