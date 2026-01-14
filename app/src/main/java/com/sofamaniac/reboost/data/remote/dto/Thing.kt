/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 11:40 PM
 *
 */

package com.sofamaniac.reboost.data.remote.dto

import com.sofamaniac.reboost.data.remote.dto.post.PostDataFlat
import com.sofamaniac.reboost.data.remote.dto.comment.CommentData
import com.sofamaniac.reboost.reddit.ListingData
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditData
import com.sofamaniac.reboost.data.remote.dto.subreddit.dummySubredditData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("kind")
sealed class Thing {
    @Serializable
    @SerialName("t1")
    data class Comment(val data: CommentData) : Thing()

    @Serializable
    @SerialName("t2")
    data class User(val data: String) : Thing()

    @Serializable
    @SerialName("t3")
    data class Post(val data: PostDataFlat) :
        Thing()

    @Serializable
    @SerialName("t5")
    data class Subreddit(val data: SubredditData = dummySubredditData()) :
        Thing()

    @Serializable
    @SerialName("Listing")
    data class Listing<T : Thing>(
        val data: ListingData<T>
    ) : Thing(), Iterable<T> {

        val size: Int get() = data.children.size

        override fun iterator(): Iterator<T> {
            return data.children.iterator()
        }

        fun isEmpty(): Boolean {
            return data.children.isEmpty()
        }
    }

    @Serializable
    @SerialName("more")
    data class More(val data: MoreData) : Thing()
}


fun emptyListing(): Thing.Listing<Thing> {
    return Thing.Listing(data = ListingData())
}




@Serializable
data class MoreData(
    val count: Int,
    val name: String,
    val id: String,
    val parent_id: String,
    val depth: Int,
    val children: List<String>,
)