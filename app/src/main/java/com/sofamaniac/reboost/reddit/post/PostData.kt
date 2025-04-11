package com.sofamaniac.reboost.reddit.post

import com.sofamaniac.reboost.reddit.AuthorInfo
import com.sofamaniac.reboost.reddit.Flair
import com.sofamaniac.reboost.reddit.Thumbnail
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PostData(
    val id: PostId,
    val name: PostFullname,
    val url: String,
    val domain: String,
    val permalink: String,
    val title: String,
    val suggestedSort: String,
    val numComments: Int,
    val over18: Boolean,
    val preview: Preview,
    val author: AuthorInfo,
    val subreddit: SubredditInfo,
    val thumbnail: Thumbnail,
    val score: Score,
    val selftext: Selftext,
    val kind: Kind,
    val linkFlair: Flair,
    val media: MediaInfo,
    //val status: Status
    val createdAt: Instant,
    val edited: Instant?,
    val relationship: Relationship,
)


