/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:44 PM
 *
 */

package com.sofamaniac.reboost.data.remote.dto.subreddit

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.sofamaniac.reboost.data.remote.dto.Thing.Subreddit

@Entity
data class SubredditEntity(
    @PrimaryKey val id: SubredditId,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "display_name") val displayName: SubredditName,
    @ColumnInfo(name = "display_name_prefixed") val displayNamePrefixed: String,
    @ColumnInfo(name = "icon_img") val iconImg: String,
    @ColumnInfo(name = "primary_color") val primaryColor: String? = "#aaaaaa",
) {
    companion object {
        fun fromSubreddit(subreddit: Subreddit): SubredditEntity {
            val iconUrl: String =
                subreddit.data.community_icon ?: subreddit.data.icon_img
                ?: ""
            return SubredditEntity(
                id = subreddit.data.id,
                name = subreddit.data.name,
                displayName = subreddit.data.display_name,
                displayNamePrefixed = subreddit.data.display_name_prefixed,
                iconImg = iconUrl,
                primaryColor = subreddit.data.primary_color
            )
        }
    }
}

@Dao
interface SubredditDao {
    @Query("SELECT * FROM SubredditEntity WHERE id = :id")
    fun getById(id: SubredditId): SubredditEntity?

    @Query("SELECT * FROM SubredditEntity WHERE display_name = :name")
    fun getByName(name: SubredditName): SubredditEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(subreddit: SubredditEntity)

}