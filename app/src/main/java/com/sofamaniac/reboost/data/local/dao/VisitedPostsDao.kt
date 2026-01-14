package com.sofamaniac.reboost.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sofamaniac.reboost.data.local.entities.VisitedPostEntity

@Dao
interface VisitedPostsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: VisitedPostEntity)

    @Query("SELECT * FROM visitedPosts WHERE id = :id")
    fun getPost(id: String): VisitedPostEntity?
}
