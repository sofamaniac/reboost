package com.sofamaniac.reboost.reddit.post

import com.sofamaniac.reboost.reddit.AuthorInfo
import com.sofamaniac.reboost.reddit.Flair
import com.sofamaniac.reboost.reddit.Thumbnail
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PostData(
    val id: String,
    val name: String,
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

@Serializable
data class Relationship(
    val clicked: Boolean,
    val visited: Boolean,
    val saved: Boolean,
    val liked: Boolean?
)


@Serializable
data class Score(
    val ups: Int,
    val downs: Int,
    val score: Int,
    val upvoteRatio: Double,
    val hideScore: Boolean
)

@Serializable
data class Selftext(
    val selftext: String,
    val selftextHtml: String
)

@Serializable
data class SubredditInfo(
    val subreddit: String,
    val subredditId: String,
    val subredditPrefixed: String,
    val subredditSubscribers: Int,
    val subredditType: String
)

@Serializable
data class MediaInfo(
    val media: Media? = null,
    val mediaEmbed: Map<String, String> = emptyMap(),
    val mediaMetadata: Map<String, MediaMetadata>? = null,
    val mediaOnly: Boolean = false,
)