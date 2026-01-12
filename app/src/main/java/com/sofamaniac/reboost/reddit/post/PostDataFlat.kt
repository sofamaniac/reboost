/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 11:42 PM
 *
 */

package com.sofamaniac.reboost.reddit.post

import com.sofamaniac.reboost.reddit.AuthorInfo
import com.sofamaniac.reboost.reddit.Flair
import com.sofamaniac.reboost.reddit.LinkFlairRichtext
import com.sofamaniac.reboost.reddit.Thumbnail
import com.sofamaniac.reboost.reddit.subreddit.SubredditDetails
import com.sofamaniac.reboost.reddit.subreddit.SubredditId
import com.sofamaniac.reboost.reddit.subreddit.SubredditName
import com.sofamaniac.reboost.reddit.utils.FalseOrTimestampSerializer
import com.sofamaniac.reboost.reddit.utils.InstantAsFloatSerializer
import com.sofamaniac.reboost.reddit.utils.MediaMetadataSerializer
import com.sofamaniac.reboost.reddit.utils.TranscodedVideo
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import tech.mappie.api.ObjectMappie
import java.net.URI
import java.net.URL
import java.util.Collections.emptyList
import kotlin.time.Instant


/** ID 36 of a post */
@Serializable
@JvmInline
value class PostId(val id: String)

/**
 * The string "t3_ID36" of a post
 * @see PostId
 */
@Serializable
@JvmInline
value class PostFullname(val id: String)

@Serializable
data class PostDataFlat(

    @SerialName("id") val id: PostId,
    @SerialName("name") val fullname: PostFullname,
    @SerialName("url") @Contextual val url: URL? = null,
    @SerialName("title") val title: String = "",
    @SerialName("suggested_sort") val suggestedSort: String? = null,
    @SerialName("num_comments") val numComments: Int = 0,
    @SerialName("over_18") val over18: Boolean = false,
    @SerialName("permalink") val permalink: String = "",
    @SerialName("post_hint") val postHint: String? = null,
    @SerialName("pinned") val pinned: Boolean = false,
    @SerialName("preview") val preview: Preview? = null,
    @SerialName("gallery_data") val galleryData: GalleryData? = null,


    // ================================================ //
    // AUTHOR INFORMATION
    // ================================================ //
    @SerialName("author") val author: String? = "",
    @SerialName("author_fullname") val authorFullname: String = "",
    @SerialName("author_is_blocked") val authorIsBlocked: Boolean = false,
    @SerialName("author_patreon_flair") val authorPatreonFlair: Boolean = false,
    @SerialName("author_premium") val authorPremium: Boolean = false,

    // Author flair
    @SerialName("author_flair_background_color") val authorFlairBackgroundColor: String? = null,
    @SerialName("author_flair_css_class") val authorFlairCssClass: String? = null,
    @SerialName("author_flair_richtext") val authorFlairRichtext: List<LinkFlairRichtext> = emptyList(),
    @SerialName("author_flair_template_id") val authorFlairTemplateId: String? = null,
    @SerialName("author_flair_text") val authorFlairText: String? = null,
    @SerialName("author_flair_text_color") val authorFlairTextColor: String? = null,
    @SerialName("author_flair_type") val authorFlairType: String? = null,

    // Approved info
    @SerialName("approved_at_utc") val approvedAtUtc: String? = null,
    @SerialName("approved_by") val approvedBy: String? = null,

    // Ban Info
    @Serializable(with = InstantAsFloatSerializer::class)
    @SerialName("banned_at_utc") val bannedAtUtc: Instant = Instant.DISTANT_FUTURE,
    @SerialName("banned_by") val bannedBy: String? = null,

    // ================================================ //
    // SUBREDDIT INFORMATION
    // ================================================ //
    @SerialName("subreddit") val subreddit: SubredditName,
    @SerialName("subreddit_id") val subredditId: SubredditId,
    @SerialName("subreddit_name_prefixed") val subredditNamePrefixed: String = "",
    @SerialName("subreddit_subscribers") val subredditSubscribers: Int = 0,
    @SerialName("subreddit_type") val subredditType: String = "",
    @SerialName("sr_detail") val subredditDetails: SubredditDetails? = null,

    // ================================================ //
    // THUMBNAIL INFORMATION
    // ================================================ //
    /** Can either be an URL, "self", "spoiler" */
    @SerialName("thumbnail") @Contextual val thumbnailUrl: URI? = null,
    @SerialName("thumbnail_height") val thumbnail_height: Int? = null,
    @SerialName("thumbnail_width") val thumbnail_width: Int? = null,

    // ================================================ //
    // SCORE INFORMATION
    // ================================================ //
    @SerialName("downs") val downs: Int = 0,
    @SerialName("score") val scoreInt: Int = 0,
    @SerialName("ups") val ups: Int = 0,
    @SerialName("upvote_ratio") val upvote_ratio: Double = 0.0,
    @SerialName("hide_score") val hide_score: Boolean = false,

    // Awards
    @SerialName("all_awardings") val allAwardings: List<String> = emptyList(),
    @SerialName("awarders") val awarders: List<String> = emptyList(),
    @SerialName("gilded") val gilded: Int = 0,
    @SerialName("gildings") val gildings: Map<String, Int> = emptyMap(),
    @SerialName("top_awarded_type") val top_awarded_type: String? = null,
    @SerialName("total_awards_received") val total_awards_received: Int = 0,

    // Selftext
    @SerialName("selftext") val selftextRaw: String = "",
    @SerialName("selftext_html") val selftext_html: String? = null,

    // Creation Info
    @SerialName("created") val created: Double = 0.0,
    @Serializable(with = InstantAsFloatSerializer::class)
    @SerialName("created_utc") val created_utc: Instant = Instant.DISTANT_FUTURE,

    // Relationship
    @SerialName("clicked") val clicked: Boolean = false,
    @SerialName("visited") val visited: Boolean = false,
    @SerialName("likes") var likes: Boolean? = null,
    @SerialName("saved") var saved: Boolean = false,

    // Status
    @SerialName("archived") val archived: Boolean = false,
    @Serializable(with = FalseOrTimestampSerializer::class)
    @SerialName("edited") val edited: Long? = null,
    @SerialName("hidden") val hidden: Boolean = false,
    @SerialName("locked") val locked: Boolean = false,
    @SerialName("is_crosspostable") val is_crosspostable: Boolean = false,
    @SerialName("is_created_from_ads_ui") val is_created_from_ads_ui: Boolean = false,
    @SerialName("can_gild") val can_gild: Boolean = false,
    @SerialName("can_mod_post") val can_mod_post: Boolean = false,
    @SerialName("spoiler") val spoiler: Boolean = false,
    @SerialName("stickied") val stickied: Boolean = false,

    @SerialName("category") val category: String? = null,
    @SerialName("content_categories") val content_categories: List<String>? = null,
    @SerialName("contest_mode") val contest_mode: Boolean = false,
    @SerialName("discussion_type") val discussion_type: String? = null,
    @SerialName("distinguished") val distinguished: String? = null,
    @SerialName("domain") val domain: String = "",

    // Kind
    @SerialName("is_gallery") val isGallery: Boolean = false,
    @SerialName("is_meta") val is_meta: Boolean = false,
    @SerialName("is_original_content") val is_original_content: Boolean = false,
    @SerialName("is_reddit_media_domain") val is_reddit_media_domain: Boolean = false,
    @SerialName("is_robot_indexable") val is_robot_indexable: Boolean = false,
    @SerialName("is_self") val is_self: Boolean = false,
    @SerialName("is_video") val is_video: Boolean = false,

    // ================================================ //
    // FLAIR INFORMATION
    // ================================================ //
    @SerialName("link_flair_background_color") val link_flair_background_color: String? = null,
    @SerialName("link_flair_css_class") val link_flair_css_class: String? = null,
    @SerialName("link_flair_richtext") val link_flair_richtext: List<LinkFlairRichtext> = emptyList(),
    @SerialName("link_flair_text") val link_flair_text: String? = null,
    @SerialName("link_flair_text_color") val link_flair_text_color: String? = null,
    @SerialName("link_flair_type") val link_flair_type: String? = null,

    // Media
    @SerialName("media") val media: Media? = null,
    @SerialName("media_embed") val media_embed: Map<String, String> = emptyMap(),
    @SerialName("media_metadata") val media_metadata: Map<String, MediaMetadata?> = emptyMap(),
    @SerialName("media_only") val media_only: Boolean = false,
    // FIXME
    //@SerialName("secure_media") val secure_media: Map<String, String> = emptyMap(),
    @SerialName("secure_media_embed") val secure_media_embed: Map<String, String> = emptyMap(),

    // ================================================ //
    // MODERATION INFORMATION
    // ================================================ //
    @SerialName("mod_note") val mod_note: String? = null,
    @SerialName("mod_reason_by") val mod_reason_by: String? = null,
    @SerialName("mod_reason_title") val mod_reason_title: String? = null,
    @SerialName("mod_reports") val mod_reports: List<String> = emptyList(),
    // Removal info
    @SerialName("removal_reason") val removal_reason: String? = null,
    @SerialName("removed_by") val removed_by: String? = null,
    @SerialName("removed_by_category") val removed_by_category: String? = null,

    // ================================================ //
    // OTHER INFORMATION
    // ================================================ //
    @SerialName("no_follow") val no_follow: Boolean = false,
    @SerialName("num_crossposts") val num_crossposts: Int = 0,
    @SerialName("num_reports") val num_reports: Int? = null,
    @SerialName("pwls") val pwls: Int? = null,
    @SerialName("quarantine") val quarantine: Boolean = false,
    @SerialName("report_reasons") val report_reasons: String? = null,
    @SerialName("send_replies") val send_replies: Boolean = false,
    @SerialName("treatment_tags") val treatment_tags: List<String> = emptyList(),
    @SerialName("user_reports") val user_reports: List<String> = emptyList(),
    @SerialName("view_count") val view_count: String? = null,
    @SerialName("wls") val wls: Int? = null,
    @SerialName("allow_live_comments") val allowLiveComments: Boolean = false,
)

private fun PostDataFlat.toAuthorInfo() = AuthorInfo(
    username = author ?: "",
    flair = this.toAuthorFlair(),
    authorFullname = authorFullname,
    isAuthorBlocked = authorIsBlocked,
    hasPatreonFlair = authorPatreonFlair,
    isAuthorPremium = authorPremium
)

private fun PostDataFlat.toAuthorFlair() = Flair(
    text = authorFlairText ?: "",
    backgroundColor = authorFlairBackgroundColor ?: "",
    textColor = authorFlairTextColor ?: "",
    richText = authorFlairRichtext,
    type = authorFlairType ?: ""
)

private fun PostDataFlat.toSubredditInfo() = SubredditInfo(
    name = subreddit,
    subredditId = subredditId,
    subredditPrefixed = subredditNamePrefixed,
    subredditSubscribers = subredditSubscribers,
    subredditType = subredditType
)

private fun PostDataFlat.toThumbnail() = Thumbnail(
    uri = thumbnailUrl ?: URI(""),
    width = thumbnail_width ?: 0,
    height = thumbnail_height ?: 0
)

private fun PostDataFlat.toScore() = Score(
    ups = ups,
    downs = downs,
    score = scoreInt,
    upvoteRatio = upvote_ratio,
    hideScore = hide_score
)

private fun PostDataFlat.toSelftext() = Selftext(
    selftext = selftextRaw,
    selftextHtml = selftext_html ?: ""
)

private fun PostDataFlat.toLinkFlair() = Flair(
    text = link_flair_text ?: "",
    backgroundColor = link_flair_background_color ?: "",
    textColor = link_flair_text_color ?: "",
    richText = link_flair_richtext,
    type = link_flair_type ?: ""
)

private fun PostDataFlat.toMediaInfo() = MediaInfo(
    media = media,
    mediaEmbed = media_embed,
    mediaMetadata = media_metadata,
    mediaOnly = media_only
)

private fun PostDataFlat.toRelationship() = Relationship(
    clicked = clicked,
    visited = visited,
    saved = saved,
    liked = likes
)


object PostDataMapper : ObjectMappie<PostDataFlat, PostData>() {

    override fun map(from: PostDataFlat) = mapping {

        PostData::name fromProperty from::fullname

        PostData::url fromValue (from.url ?: URL(""))
        PostData::suggestedSort fromValue (from.suggestedSort ?: "")
        PostData::preview fromValue (from.preview ?: Preview())

        PostData::author fromValue from.toAuthorInfo()
        PostData::subreddit fromValue from.toSubredditInfo()
        PostData::thumbnail fromValue from.toThumbnail()
        PostData::score fromValue from.toScore()
        PostData::selftext fromValue from.toSelftext()
        PostData::linkFlair fromValue from.toLinkFlair()
        PostData::media fromValue from.toMediaInfo()
        PostData::relationship fromValue from.toRelationship()
        PostData::kind fromValue getKind(from)

        PostData::createdAt fromProperty from::created_utc
        PostData::edited fromValue Instant.fromEpochSeconds(from.edited ?: 0)

        PostData::galleryData fromValue (from.galleryData?.items ?: emptyList())
    }
}


//FIXME
@Serializable
data class Media(
    @Serializable(with = TranscodedVideo::class)
    val reddit_video: RedditVideo? = null,
    val oembed: OEmbed? = null,
)

// FIXME: there is a field called cache_age that apparently can exceed Int.MAX_VALUE, making the serializer crash ?
@Serializable
data class OEmbed(
    @SerialName("provider_url") @Contextual val providerUrl: URL? = null,
    @SerialName("version") val version: String = "",
    @SerialName("title") val title: String = "",
    /** One of "video" */
    @SerialName("type") val type: String = "",
    @SerialName("height") val height: Int = 0,
    @SerialName("width") val width: Int = 0,
    @SerialName("html") val html: String = "",
    @SerialName("provider_name") val providerName: String = "",

    @SerialName("thumbnail_width") val thumbnailWidth: Int = 0,
    @SerialName("thumbnail_height") val thumbnailHeight: Int = 0,
    @SerialName("thumbnail_url") @Contextual val thumbnailUrl: URL? = null,

    @SerialName("cache_age") val cacheAge: Int = 0,
)


@Serializable
data class RedditVideo(
    /** Use this url to get the video */
    @Contextual val fallback_url: URL,
    val width: Int = 0,
    val height: Int = 0,
    @Contextual val scrubber_media_url: URL,
    @Contextual val dash_url: URL,
    /** Duration in seconds */
    val duration: Int,
    @Contextual val hls_url: URL? = null,
    val is_gif: Boolean = false,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = MediaMetadataSerializer::class)
@JsonClassDiscriminator("e")
sealed class MediaMetadata() {
    @Serializable
    @SerialName("Image")
    data class Image(
        @SerialName("id") val id: String? = null,
        /** Something like "image/jpeg" */
        @SerialName("m") val m: String? = null,
        @SerialName("p") val preview: List<MediaPreview> = emptyList(),
        /** Source ? */
        @SerialName("s") val s: MediaPreview? = null,
        /** Original ? */
        @SerialName("o") val o: List<MediaPreview> = emptyList(),
    ) : MediaMetadata()

    @Serializable
    @SerialName("AnimatedImage")
    data class Gif(
        @SerialName("id") val id: String? = null,
        /** Something like "image/jpeg" */
        @SerialName("m") val m: String? = null,
        @SerialName("p") val preview: List<MediaPreview> = emptyList(),
        /** Source ? */
        @SerialName("s") val s: MediaPreviewGifSource? = null,
        /** Original ? */
        @SerialName("o") val o: List<MediaPreview> = emptyList(),
    ) : MediaMetadata()

    @Serializable
    @SerialName("Invalid")
    object Invalid : MediaMetadata()
}

@Serializable
data class MediaPreviewGifSource(
    @SerialName("gif") @Contextual val gifUrl: URL? = null,
    @SerialName("x") val width: Int = 0,
    @SerialName("y") val height: Int = 0,
    @SerialName("mp4") @Contextual val mp4Url: URL? = null,
) {
    val ratio: Float
        get() = width.toFloat() / height.toFloat()
}


@Serializable
data class MediaPreview(
    /** Url of the preview */
    @SerialName("u") @Contextual val url: URL? = null,
    @SerialName("x") val width: Int = 0,
    @SerialName("y") val height: Int = 0
) {
    val ratio: Float
        get() = width.toFloat() / height.toFloat()
}
