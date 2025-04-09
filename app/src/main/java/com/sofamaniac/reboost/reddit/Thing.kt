package com.sofamaniac.reboost.reddit

import com.sofamaniac.reboost.reddit.comment.CommentData
import com.sofamaniac.reboost.reddit.post.Kind
import com.sofamaniac.reboost.reddit.post.PostData
import com.sofamaniac.reboost.reddit.post.getKind
import com.sofamaniac.reboost.reddit.subreddit.SubredditData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("kind")
sealed class Thing

@Serializable
@SerialName("t3")
data class Post(val data: PostData) : Thing() {
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
        return if (
            (data.thumbnail_width ?: 0) > 0
            && (data.thumbnail_height ?: 0) > 0
            && data.thumbnail != null
        ) {
            data.thumbnail
        } else {
            null
        }
    }

    fun authorFlair(): Flair {
        return Flair(
            text = data.author_flair_text ?: "",
            backgroundColor = data.author_flair_background_color ?: "",
            textColor = data.author_flair_text_color ?: "",
            richText = data.author_flair_richtext,
            type = data.author_flair_type ?: ""
        )
    }

    fun linkFlair(): Flair {
        return Flair(
            text = data.link_flair_text ?: "",
            backgroundColor = data.link_flair_background_color ?: "",
            textColor = data.link_flair_text_color ?: "",
            richText = data.link_flair_richtext,
            type = data.link_flair_type ?: ""
        )
    }
}

@Serializable
@SerialName("Listing")
data class Listing<T : Thing>(
    val data: ListingData<T>
) : Thing(), Iterable<T> {
    override fun iterator(): Iterator<T> {
        return data.children.iterator()
    }

    fun size(): Int {
        return data.children.size
    }
}

@Serializable
@SerialName("t5")
data class Subreddit(val data: SubredditData = SubredditData()) :
    Thing()

@Serializable
@SerialName("t1")
data class Comment(val data: CommentData) : Thing()

@Serializable
@SerialName("t2")
data class User(val data: String) : Thing()

@Serializable
@SerialName("more")
data class More(val data: MoreData) : Thing()

@Serializable
data class MoreData(
    val count: Int,
    val name: String,
    val id: String,
    val parent_id: String,
    val depth: Int,
    val children: List<String>
)



