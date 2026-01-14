package com.sofamaniac.reboost.domain.model

import com.sofamaniac.reboost.data.remote.dto.post.GalleryDataImage
import com.sofamaniac.reboost.data.remote.dto.post.PostFullname
import com.sofamaniac.reboost.data.remote.dto.post.PostId
import com.sofamaniac.reboost.data.remote.dto.post.Preview
import com.sofamaniac.reboost.domain.model.AuthorInfo
import com.sofamaniac.reboost.domain.model.Flair
import com.sofamaniac.reboost.reddit.Thumbnail
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditDetails
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.net.URL
import kotlin.time.Instant

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
    val subredditDetails: SubredditDetails?,
    val thumbnail: Thumbnail,
    val score: Score,
    val selftext: Selftext,
    val kind: Kind,
    val linkFlair: Flair,
    val media: MediaInfo,
    val galleryData: List<GalleryDataImage>,
    //val status: Status
    val createdAt: Instant,
    val edited: Instant?,
    val relationship: Relationship,
)