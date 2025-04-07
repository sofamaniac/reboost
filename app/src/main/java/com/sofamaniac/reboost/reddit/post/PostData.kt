package com.sofamaniac.reboost.reddit.post

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.longOrNull

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
    @SerialName("banned_at_utc") val banned_at_utc: Instant? = null,
    @SerialName("banned_by") val banned_by: String? = null,
    @SerialName("can_gild") val can_gild: Boolean = false,
    @SerialName("can_mod_post") val can_mod_post: Boolean = false,
    @SerialName("category") val category: String? = null,
    @SerialName("clicked") val clicked: Boolean = false,
    @SerialName("is_gallery") val isGallery: Boolean = false,
    @SerialName("content_categories") val content_categories: List<String>? = null,
    @SerialName("contest_mode") val contest_mode: Boolean = false,
    @SerialName("created") val created: Double = 0.0,
    @Serializable(with = InstantAsFloatSerializer::class)
    @SerialName("created_utc") val created_utc: Instant = Instant.DISTANT_FUTURE,
    @SerialName("discussion_type") val discussion_type: String? = null,
    @SerialName("distinguished") val distinguished: String? = null,
    @SerialName("domain") val domain: String = "",
    @SerialName("downs") val downs: Int = 0,
    @Serializable(with = FalseOrTimestampSerializer::class)
    @SerialName("edited") val edited: Long? = null,
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
    @SerialName("likes") var likes: Boolean? = null,
    @SerialName("link_flair_background_color") val link_flair_background_color: String? = null,
    @SerialName("link_flair_css_class") val link_flair_css_class: String? = null,
    @SerialName("link_flair_richtext") val link_flair_richtext: List<LinkFlairRichtext> = emptyList(),
    @SerialName("link_flair_text") val link_flair_text: String? = null,
    @SerialName("link_flair_text_color") val link_flair_text_color: String? = null,
    @SerialName("link_flair_type") val link_flair_type: String? = null,
    @SerialName("locked") val locked: Boolean = false,
    @SerialName("media") val media: Media? = null,
    @SerialName("media_embed") val media_embed: Map<String, String> = emptyMap(),
    @SerialName("media_metadata") val media_metadata: Map<String, MediaMetadata>? = null,
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
    @SerialName("saved") var saved: Boolean = false,
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

//FIXME
@Serializable
data class Media(
    val reddit_video: RedditVideo? = null,
)


@Serializable
data class RedditVideo(
    val fallback_url: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val scrubber_media_url: String = "",
    val dash_url: String = "",
    /** Duration in seconds */
    val duration: Int,
    val hls_url: String = "",
    val is_gif: Boolean = false,
    val transcoding_status: String = "",
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
    @SerialName("e") val e: String? = null,
    @SerialName("id") val id: String? = null,
    @SerialName("m") val m: String? = null,
    @SerialName("p") val p: List<MediaPreview>? = null,
    @SerialName("s") val s: MediaPreview? = null,
    /** "failed" or "success", the other field are present only if "success" */
    @SerialName("status") val status: String
)

@Serializable
data class MediaPreview(
    /** Url of the preview */
    @SerialName("u") val url: String = "",
    @SerialName("x") val width: Int = 0,
    @SerialName("y") val height: Int = 0
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

object FalseOrTimestampSerializer : KSerializer<Long?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FalseOrTimestamp", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Long? {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        val element = jsonDecoder.decodeJsonElement()
        return when (element) {
            is JsonPrimitive -> {
                if (element.isString && element.content == "false") null
                else element.longOrNull
            }

            else -> null
        }
    }

    override fun serialize(encoder: Encoder, value: Long?) {
        encoder.encodeLong(value ?: 0L)
    }
}

object InstantAsFloatSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("InstantAsFloat", PrimitiveKind.FLOAT)

    override fun deserialize(decoder: Decoder): Instant {
        val timestamp = decoder.decodeDouble() // Use decodeDouble to handle both Float & Double
        return Instant.fromEpochSeconds(timestamp.toLong())
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeDouble(value.toEpochMilliseconds().toDouble())
    }
}
