package com.sofamaniac.reboost.reddit.post

import com.sofamaniac.reboost.reddit.Thing
import kotlinx.serialization.Serializable

@Serializable
class Post(override val data: PostData) : Thing<PostData>(kind = "t3") {
    override fun fullname(): String {
        return data.name
    }

    override fun id(): String {
        return data.id
    }

    fun upvotes(): Int {
        return data.ups
    }

    fun downvotes(): Int {
        return data.downs
    }

    fun score(): Int {
        return data.score
    }

    fun kind(): Kind {
        return getKind(this)
    }

    fun thumbnail(): String? {
        return if ((data.thumbnail_width ?: 0) > 0 && (data.thumbnail_height
                ?: 0) > 0 && data.thumbnail != null
        ) {
            data.thumbnail
        } else {
            null
        }
    }
}
