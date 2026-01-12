/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:36 PM
 *
 */

package com.sofamaniac.reboost.reddit.post

import com.sofamaniac.reboost.reddit.AuthorInfo
import com.sofamaniac.reboost.reddit.Flair
import com.sofamaniac.reboost.reddit.Thumbnail
import com.sofamaniac.reboost.reddit.subreddit.SubredditDetails
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.net.URL
import kotlin.time.Instant

@Serializable
data class PostData(
    val id: PostId,
    val name: PostFullname,
    @Contextual val url: URL,
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


