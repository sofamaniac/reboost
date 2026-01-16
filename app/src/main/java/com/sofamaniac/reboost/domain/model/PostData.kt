package com.sofamaniac.reboost.domain.model

import com.sofamaniac.reboost.data.remote.dto.post.PostFullname
import com.sofamaniac.reboost.data.remote.dto.post.PostId
import com.sofamaniac.reboost.data.remote.dto.post.Preview
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditDetails
import com.sofamaniac.reboost.reddit.Thumbnail
import kotlinx.serialization.Serializable
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
    val _preview: Preview?,
    val crosspostParentList: List<PostData>,
    val author: AuthorInfo,
    val subreddit: SubredditInfo,
    val subredditDetails: SubredditDetails?,
    val thumbnail: Thumbnail,
    val score: Score,
    val selftext: Selftext,
    val kind: Kind,
    val linkFlair: Flair,
    val media: MediaInfo,
    val gallery: Gallery,
    //val status: Status
    val createdAt: Instant,
    val edited: Instant?,
    val relationship: Relationship,
) {
    fun getPreview(): Preview? {
        return _preview ?: crosspostParentList.firstOrNull()?.getPreview()
    }

    fun getGalleryData(): Gallery {
        if (gallery.images.isNotEmpty()) return gallery
        return crosspostParentList.firstOrNull()?.getGalleryData() ?: Gallery(
            emptyList(),
            emptyMap()
        )
    }

    val isCrosspost: Boolean
        get() {
            return crosspostParentList.isNotEmpty()
        }
}