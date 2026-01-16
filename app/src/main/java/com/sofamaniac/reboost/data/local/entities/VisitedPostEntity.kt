package com.sofamaniac.reboost.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.reboost.domain.model.PostData
import kotlinx.serialization.json.Json

@Entity(tableName = "visitedPosts")
class VisitedPostEntity (
    @PrimaryKey
    var id: String = "",
    var post: String,
)

fun VisitedPostEntity.toDomainModel(): PostData {
    return Json.decodeFromString(post)
}

fun PostData.toEntity(): VisitedPostEntity {
    return VisitedPostEntity(id = id.id, post = Json.encodeToString(this))
}