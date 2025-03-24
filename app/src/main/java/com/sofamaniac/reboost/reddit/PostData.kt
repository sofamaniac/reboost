package com.sofamaniac.reboost.reddit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostData(
    @SerialName("all_awardings") val allAwardings: List<String> = emptyList(),
    @SerialName("allow_live_comments") val allowLiveComments: Boolean = false,
    @SerialName("approved_at_utc") val approvedAtUtc: String? = null,
    @SerialName("approved_by") val approvedBy: String? = null,
    @SerialName("archived") val archived: Boolean = false,
    // Author related fields
    @SerialName("author") val author: String? = "",
    @SerialName("author_flair_background_color") val author_flair_background_color: String? = null,
    @SerialName("author_flair_css_class") val author_flair_css_class: String? = null,
    @SerialName("author_flair_richtext") val author_flair_richtext: List<LinkFlairRichtext> = emptyList(),
    @SerialName("author_flair_template_id") val author_flair_template_id: String? = null,
    @SerialName("author_flair_text") val author_flair_text: String? = null,
    @SerialName("author_flair_text_color") val author_flair_text_color: String? = null,
    @SerialName("author_flair_type") val author_flair_type: String? = null,
    @SerialName("author_fullname") val author_fullname: String = "",
    @SerialName("author_is_blocked") val author_is_blocked: Boolean = false,
    @SerialName("author_patreon_flair") val author_patreon_flair: Boolean = false,
    @SerialName("author_premium") val author_premium: Boolean = false,

    @SerialName("awarders") val awarders: List<String> = emptyList(),
    @SerialName("banned_at_utc") val banned_at_utc: String? = null,
    @SerialName("banned_by") val banned_by: String? = null,
    @SerialName("can_gild") val can_gild: Boolean = false,
    @SerialName("can_mod_post") val can_mod_post: Boolean = false,
    @SerialName("category") val category: String? = null,
    @SerialName("clicked") val clicked: Boolean = false,
    @SerialName("is_gallery") val isGallery: Boolean = false,
    @SerialName("content_categories") val content_categories: List<String>? = null,
    @SerialName("contest_mode") val contest_mode: Boolean = false,
    @SerialName("created") val created: Double = 0.0,
    @SerialName("created_utc") val created_utc: Double = 0.0,
    @SerialName("discussion_type") val discussion_type: String? = null,
    @SerialName("distinguished") val distinguished: String? = null,
    @SerialName("domain") val domain: String = "",
    @SerialName("downs") val downs: Int = 0,
    // FIXME
    //@SerialName("edited") val edited: Int? = null,
    @SerialName("gilded") val gilded: Int = 0,
    @SerialName("gildings") val gildings: Map<String, Int> = emptyMap(),
    @SerialName("hidden") val hidden: Boolean = false,
    @SerialName("hide_score") val hide_score: Boolean = false,
    @SerialName("id") val id: String = "",
    @SerialName("is_created_from_ads_ui") val is_created_from_ads_ui: Boolean = false,
    @SerialName("is_crosspostable") val is_crosspostable: Boolean = false,
    @SerialName("is_meta") val is_meta: Boolean = false,
    @SerialName("is_original_content") val is_original_content: Boolean = false,
    @SerialName("is_reddit_media_domain") val is_reddit_media_domain: Boolean = false,
    @SerialName("is_robot_indexable") val is_robot_indexable: Boolean = false,
    @SerialName("is_self") val is_self: Boolean = false,
    @SerialName("is_video") val is_video: Boolean = false,
    @SerialName("likes") val likes: String? = null,
    @SerialName("link_flair_background_color") val link_flair_background_color: String? = null,
    @SerialName("link_flair_css_class") val link_flair_css_class: String? = null,
    @SerialName("link_flair_richtext") val link_flair_richtext: List<LinkFlairRichtext> = emptyList(),
    @SerialName("link_flair_text") val link_flair_text: String? = null,
    @SerialName("link_flair_text_color") val link_flair_text_color: String? = null,
    @SerialName("link_flair_type") val link_flair_type: String? = null,
    @SerialName("locked") val locked: Boolean = false,
    // FIXME
    // @SerialName("media") val media: String? = null,
    @SerialName("media_embed") val media_embed: Map<String, String> = emptyMap(),
    @SerialName("media_metadata") val media_metadata: Map<String, MediaMetadata> = emptyMap(),
    @SerialName("media_only") val media_only: Boolean = false,
    @SerialName("mod_note") val mod_note: String? = null,
    @SerialName("mod_reason_by") val mod_reason_by: String? = null,
    @SerialName("mod_reason_title") val mod_reason_title: String? = null,
    @SerialName("mod_reports") val mod_reports: List<String> = emptyList(),
    @SerialName("name") val name: String = "",
    @SerialName("no_follow") val no_follow: Boolean = false,
    @SerialName("num_comments") val num_comments: Int = 0,
    @SerialName("num_crossposts") val num_crossposts: Int = 0,
    @SerialName("num_reports") val num_reports: Int? = null,
    @SerialName("over_18") val over_18: Boolean = false,
    @SerialName("permalink") val permalink: String = "",
    @SerialName("post_hint") val post_hint: String? = null,
    @SerialName("pinned") val pinned: Boolean = false,
    @SerialName("preview") val preview: Preview? = null,
    @SerialName("pwls") val pwls: Int? = null,
    @SerialName("quarantine") val quarantine: Boolean = false,
    @SerialName("removal_reason") val removal_reason: String? = null,
    @SerialName("removed_by") val removed_by: String? = null,
    @SerialName("removed_by_category") val removed_by_category: String? = null,
    @SerialName("report_reasons") val report_reasons: String? = null,
    @SerialName("saved") val saved: Boolean = false,
    @SerialName("score") val score: Int = 0,
    // FIXME
    //@SerialName("secure_media") val secure_media: Map<String, String> = emptyMap(),
    @SerialName("secure_media_embed") val secure_media_embed: Map<String, String> = emptyMap(),
    @SerialName("selftext") val selftext: String = "",
    @SerialName("selftext_html") val selftext_html: String? = null,
    @SerialName("send_replies") val send_replies: Boolean = false,
    @SerialName("spoiler") val spoiler: Boolean = false,
    @SerialName("stickied") val stickied: Boolean = false,
    @SerialName("subreddit") val subreddit: String = "",
    @SerialName("subreddit_id") val subreddit_id: String = "",
    @SerialName("subreddit_name_prefixed") val subreddit_name_prefixed: String = "",
    @SerialName("subreddit_subscribers") val subreddit_subscribers: Int = 0,
    @SerialName("subreddit_type") val subreddit_type: String = "",
    @SerialName("suggested_sort") val suggested_sort: String? = null,
    @SerialName("thumbnail") val thumbnail: String? = null,
    @SerialName("thumbnail_height") val thumbnail_height: Int? = null,
    @SerialName("thumbnail_width") val thumbnail_width: Int? = null,
    @SerialName("title") val title: String = "",
    @SerialName("top_awarded_type") val top_awarded_type: String? = null,
    @SerialName("total_awards_received") val total_awards_received: Int = 0,
    @SerialName("treatment_tags") val treatment_tags: List<String> = emptyList(),
    @SerialName("ups") val ups: Int = 0,
    @SerialName("upvote_ratio") val upvote_ratio: Double = 0.0,
    @SerialName("url") val url: String = "",
    @SerialName("user_reports") val user_reports: List<String> = emptyList(),
    @SerialName("view_count") val view_count: String? = null,
    @SerialName("visited") val visited: Boolean = false,
    @SerialName("wls") val wls: Int? = null
)

@Serializable
data class LinkFlairRichtext(
    @SerialName("e") val e: String,
    @SerialName("t") val t: String? = null,
    @SerialName("a") val a: String? = null,
    @SerialName("u") val u: String? = null,
)

@Serializable
data class MediaMetadata(
    @SerialName("e") val e: String,
    @SerialName("id") val id: String,
    @SerialName("m") val m: String,
    @SerialName("p") val p: List<MediaPreview> = emptyList(),
    @SerialName("s") val s: MediaPreview,
    @SerialName("status") val status: String
)

@Serializable
data class MediaPreview(
    @SerialName("u") val u: String = "",
    @SerialName("x") val x: Int = 0,
    @SerialName("y") val y: Int = 0
)

@Serializable
data class Preview(
    @SerialName("images") val images: List<PreviewImage> = emptyList(),
    val enabled: Boolean? = false
)

@Serializable
data class PreviewImage(
    @SerialName("source") val source: PreviewImageSource,
    @SerialName("resolutions") val resolutions: List<PreviewImageSource>,
    // val variant
    val id: String? = null,
)

@Serializable
data class PreviewImageSource(
    @SerialName("url") val url: String,
    val width: Int,
    val height: Int,
)
