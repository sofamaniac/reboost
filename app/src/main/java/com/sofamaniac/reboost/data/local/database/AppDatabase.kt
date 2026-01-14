package com.sofamaniac.reboost.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sofamaniac.reboost.data.local.dao.AccountsDao
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.local.entities.RedditAccountEntity
import com.sofamaniac.reboost.data.local.entities.VisitedPostEntity

@Database(entities = [RedditAccountEntity::class, VisitedPostEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountsDao(): AccountsDao
    abstract fun visitedPostsDao(): VisitedPostsDao
}