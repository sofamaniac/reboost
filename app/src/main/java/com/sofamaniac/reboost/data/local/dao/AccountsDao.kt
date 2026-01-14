package com.sofamaniac.reboost.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sofamaniac.reboost.data.local.entities.RedditAccountEntity


@Dao
interface AccountsDao {
    @Query("SELECT * FROM accounts")
    fun getAll(): List<RedditAccountEntity>

    @Query("SELECT * FROM accounts WHERE isActive = 1 LIMIT 1")
    fun getActiveAccount(): RedditAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(account: RedditAccountEntity)

    @Query("UPDATE accounts SET isActive = 0 WHERE isActive = 1")
    fun deactivateAll()

    @Query("UPDATE accounts SET isActive = 1 WHERE id = :accountId")
    fun activate(accountId: Int)

    @Transaction
    fun setActiveAccount(accountId: Int) {
        deactivateAll()
        activate(accountId)
    }

    @Delete
    fun delete(account: RedditAccountEntity)
}