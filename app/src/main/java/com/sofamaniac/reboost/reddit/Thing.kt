package com.sofamaniac.reboost.reddit

import com.sofamaniac.reboost.reddit.comment.CommentData
import com.sofamaniac.reboost.reddit.post.PostData
import com.sofamaniac.reboost.reddit.subreddit.SubredditData
import com.sofamaniac.reboost.reddit.subreddit.SubredditName
import com.sofamaniac.reboost.reddit.subreddit.dummySubredditData
import com.sofamaniac.reboost.reddit.utils.PostDataSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("kind")
sealed class Thing {
    abstract val data: Any
}

@Serializable
@SerialName("t3")
data class Post(@Serializable(with = PostDataSerializer::class) override val data: PostData) :
    Thing()

@Serializable
@SerialName("Listing")
data class Listing<T : Thing>(
    override val data: ListingData<T>
) : Thing(), Iterable<T> {
    override fun iterator(): Iterator<T> {
        return data.children.iterator()
    }

    fun size(): Int {
        return data.children.size
    }
}

fun emptyListing(): Listing<Thing> {
    return Listing(data = ListingData())
}

@Serializable
@SerialName("t5")
data class Subreddit(override val data: SubredditData = dummySubredditData()) :
    Thing()

@Serializable
@SerialName("t1")
data class Comment(override val data: CommentData) : Thing()

@Serializable
@SerialName("t2")
data class User(override val data: String) : Thing()

@Serializable
@SerialName("more")
data class More(override val data: MoreData) : Thing()

@Serializable
data class MoreData(
    val count: Int,
    val name: String,
    val id: String,
    val parent_id: String,
    val depth: Int,
    val children: List<String>
)