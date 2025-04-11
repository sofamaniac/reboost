package com.sofamaniac.reboost.reddit.subreddit

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.sofamaniac.reboost.reddit.Subreddit
import org.apache.commons.text.StringEscapeUtils
import androidx.core.graphics.toColorInt

@Entity
data class SubredditEntity(
    @PrimaryKey val id: SubredditId,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "display_name") val displayName: SubredditName,
    @ColumnInfo(name = "display_name_prefixed") val displayNamePrefixed: String,
    @ColumnInfo(name = "icon_img") val iconImg: String,
    @ColumnInfo(name = "primary_color") val primaryColor: String = "#aaaaaa",
) {
    companion object {
        fun fromSubreddit(subreddit: Subreddit): SubredditEntity {
            val iconUrl =
                if (!subreddit.data.icon_img.isBlank()) subreddit.data.icon_img
                else StringEscapeUtils.unescapeHtml4(subreddit.data.community_icon ?: "")
            if (iconUrl.isBlank()) {
                Log.i(
                    "SubredditEntity",
                    "No icon found for subreddit ${subreddit.data.display_name}"
                )
            }
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