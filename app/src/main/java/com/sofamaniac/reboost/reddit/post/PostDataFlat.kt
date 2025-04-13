package com.sofamaniac.reboost.reddit.post

import com.sofamaniac.reboost.reddit.AuthorInfo
import com.sofamaniac.reboost.reddit.Flair
import com.sofamaniac.reboost.reddit.LinkFlairRichtext
import com.sofamaniac.reboost.reddit.Thumbnail
import com.sofamaniac.reboost.reddit.subreddit.SubredditId
import com.sofamaniac.reboost.reddit.subreddit.SubredditName
import com.sofamaniac.reboost.reddit.utils.FalseOrTimestampSerializer
import com.sofamaniac.reboost.reddit.utils.InstantAsFloatSerializer
import com.sofamaniac.reboost.reddit.utils.TranscodedVideo
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import java.net.URI
import java.net.URL

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
    @SerialName("suggested_sort") val suggested_sort: String? = null,
    @SerialName("num_comments") val num_comments: Int = 0,
    @SerialName("over_18") val over_18: Boolean = false,
    @SerialName("permalink") val permalink: String = "",
    @SerialName("post_hint") val post_hint: String? = null,
    @SerialName("pinned") val pinned: Boolean = false,
    @SerialName("preview") val preview: Preview? = null,

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

    // Approved info
    @SerialName("approved_at_utc") val approvedAtUtc: String? = null,
    @SerialName("approved_by") val approvedBy: String? = null,

    // Ban Info
    @SerialName("banned_at_utc") val banned_at_utc: Instant? = null,
    @SerialName("banned_by") val banned_by: String? = null,

    // Subreddit info
    @SerialName("subreddit") val subreddit: SubredditName,
    @SerialName("subreddit_id") val subreddit_id: SubredditId,
    @SerialName("subreddit_name_prefixed") val subreddit_name_prefixed: String = "",
    @SerialName("subreddit_subscribers") val subreddit_subscribers: Int = 0,
    @SerialName("subreddit_type") val subreddit_type: String = "",

    // Thumbnail
    /** Can either be an URL, "self", "spoiler" */
    @SerialName("thumbnail") @Contextual val thumbnailUrl: URI? = null,
    @SerialName("thumbnail_height") val thumbnail_height: Int? = null,
    @SerialName("thumbnail_width") val thumbnail_width: Int? = null,

    // Score
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

    // Flair related fields
    @SerialName("link_flair_background_color") val link_flair_background_color: String? = null,
    @SerialName("link_flair_css_class") val link_flair_css_class: String? = null,
    @SerialName("link_flair_richtext") val link_flair_richtext: List<LinkFlairRichtext> = emptyList(),
    @SerialName("link_flair_text") val link_flair_text: String? = null,
    @SerialName("link_flair_text_color") val link_flair_text_color: String? = null,
    @SerialName("link_flair_type") val link_flair_type: String? = null,

    // Media
    @SerialName("media") val media: Media? = null,
    @SerialName("media_embed") val media_embed: Map<String, String> = emptyMap(),
    @SerialName("media_metadata") val media_metadata: Map<String, MediaMetadata>? = null,
    @SerialName("media_only") val media_only: Boolean = false,
    // FIXME
    //@SerialName("secure_media") val secure_media: Map<String, String> = emptyMap(),
    @SerialName("secure_media_embed") val secure_media_embed: Map<String, String> = emptyMap(),

    // Mod info
    @SerialName("mod_note") val mod_note: String? = null,
    @SerialName("mod_reason_by") val mod_reason_by: String? = null,
    @SerialName("mod_reason_title") val mod_reason_title: String? = null,
    @SerialName("mod_reports") val mod_reports: List<String> = emptyList(),


    // Removal info
    @SerialName("removal_reason") val removal_reason: String? = null,
    @SerialName("removed_by") val removed_by: String? = null,
    @SerialName("removed_by_category") val removed_by_category: String? = null,

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
) {
    val authorInfo: AuthorInfo
        get() {
            return AuthorInfo(
                username = author ?: "",
                flair = Flair(
                    text = author_flair_text ?: "",
                    backgroundColor = author_flair_background_color ?: "",
                    textColor = author_flair_text_color ?: "",
                    richText = author_flair_richtext,
                    type = author_flair_type ?: "",
                ),
                authorFullname = author_fullname,
                isAuthorBlocked = author_is_blocked,
                hasPatreonFlair = author_patreon_flair,
                isAuthorPremium = author_premium,
            )
        }
    val linkFlair: Flair
        get() {
            return Flair(
                text = link_flair_text ?: "",
                backgroundColor = link_flair_background_color ?: "",
                textColor = link_flair_text_color ?: "",
                richText = link_flair_richtext,
                type = link_flair_type ?: "",
            )
        }
    val thumbnail: Thumbnail
        get() {
            return Thumbnail(
                uri = thumbnailUrl ?: URI(""),
                width = thumbnail_width ?: 0,
                height = thumbnail_height ?: 0
            )
        }
    val score: Score
        get() {
            return Score(
                ups = ups,
                downs = downs,
                score = scoreInt,
                upvoteRatio = upvote_ratio,
                hideScore = hide_score
            )
        }
    val selftext: Selftext
        get() {
            return Selftext(
                selftext = selftextRaw,
                selftextHtml = selftext_html ?: ""
            )
        }
    val kind: Kind
        get() {
            return getKind(this)
        }
    val subredditInfo: SubredditInfo
        get() {
            return SubredditInfo(
                subreddit = subreddit,
                subredditId = subreddit_id,
                subredditPrefixed = subreddit_name_prefixed,
                subredditSubscribers = subreddit_subscribers,
                subredditType = subreddit_type,
            )
        }
    val mediaInfo: MediaInfo
        get() {
            return MediaInfo(
                media = media,
                mediaEmbed = media_embed,
                mediaMetadata = media_metadata,
                mediaOnly = media_only,
            )
        }
    val relationship: Relationship
        get() {
            return Relationship(
                clicked = clicked,
                visited = visited,
                saved = saved,
                liked = likes
            )
        }

    fun toPostData(): PostData {
        return PostData(
            id = id,
            url = url ?: URL(""),
            permalink = permalink,
            title = title,
            suggestedSort = suggested_sort ?: "",
            numComments = num_comments,
            over18 = over_18,
            preview = preview ?: Preview(),
            author = authorInfo,
            subreddit = subredditInfo,
            thumbnail = thumbnail,
            score = score,
            selftext = selftext,
            kind = kind,
            linkFlair = linkFlair,
            media = mediaInfo,
            createdAt = created_utc,
            edited = Instant.fromEpochSeconds(edited ?: 0),
            relationship = relationship,
            name = fullname,
            domain = domain,
        )
    }
}

//FIXME
@Serializable
data class Media(
    @Serializable(with = TranscodedVideo::class)
    val reddit_video: RedditVideo? = null,
    val oembed: OEmbed? = null,
)

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
@Serializable
@JsonClassDiscriminator("e")
sealed class MediaMetadata()

@Serializable
@SerialName("Image")
data class MediaPreviewImage(
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
data class MediaPreviewGif(
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
data class MediaPreviewGifSource(
    @SerialName("gif") @Contextual val gifUrl: URL? = null,
    @SerialName("x") val width: Int = 0,
    @SerialName("y") val height: Int = 0,
    @SerialName("mp4") @Contextual val mp4Url: URL? = null,
)


@Serializable
data class MediaPreview(
    /** Url of the preview */
    @SerialName("u") @Contextual val url: URL? = null,
    @SerialName("x") val width: Int = 0,
    @SerialName("y") val height: Int = 0
)
