package com.sofamaniac.reboost.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sofamaniac.reboost.domain.model.RedditAccount
import net.openid.appauth.AuthState

@Entity(tableName = "accounts", indices = [Index(value = ["username"], unique = true)])
data class RedditAccountEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val username: String,
    val thumbnailUrl: String,
    val authState: String,
    val isActive: Boolean = false,
)


fun RedditAccountEntity.toDomainModel(): RedditAccount {
    return RedditAccount(
        id = id,
        username = username,
        thumbnailUrl = thumbnailUrl,
        auth = AuthState.jsonDeserialize(authState),
    )
}