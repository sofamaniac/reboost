package com.sofamaniac.reboost.reddit

import kotlinx.serialization.Serializable

@Serializable
data class Subreddit(
    override val data: SubredditData = SubredditData()
) : Thing<SubredditData>(kind = "t5") {
    override fun fullname(): String {
        return data.name
    }

    override fun id(): String {
        return data.id
    }
}


@Serializable
data class CommentContributionSettings(
    val allowed_media_types: List<String>? = null
)

@Serializable
data class SubredditData(
    val user_flair_background_color: String? = null,
    val submit_text_html: String? = null,
    val restrict_posting: Boolean = false,
    val user_is_banned: Boolean = false,
    val free_form_reports: Boolean = false,
    val wiki_enabled: Boolean? = null,
    val user_is_muted: Boolean = false,
    val user_can_flair_in_sr: Boolean? = null,
    val display_name: String = "",
    val header_img: String? = null,
    val title: String = "",
    val allow_galleries: Boolean = false,
    val icon_size: List<Int>? = null,
    val primary_color: String = "",
    val active_user_count: Int? = null,
    val icon_img: String? = null,
    val display_name_prefixed: String = "",
    val accounts_active: Int? = null,
    val public_traffic: Boolean = false,
    val subscribers: Int = 0,
    val videostream_links_count: Int = 0,
    val name: String = "",
    val quarantine: Boolean = false,
    val hide_ads: Boolean = false,
    val prediction_leaderboard_entry_type: Int = 0,
    val emojis_enabled: Boolean = false,
    val advertiser_category: String = "",
    val public_description: String = "",
    val comment_score_hide_mins: Int = 0,
    val allow_predictions: Boolean = false,
    val community_icon: String? = null,
    val banner_background_image: String? = null,
    val original_content_tag_enabled: Boolean = false,
    val community_reviewed: Boolean = false,
    val submit_text: String = "",
    val description_html: String? = null,
    val spoilers_enabled: Boolean = false,
    val comment_contribution_settings: CommentContributionSettings? = null,
    val allow_talks: Boolean = false,
    val user_flair_position: String? = null,
    val all_original_content: Boolean = false,
    val has_menu_widget: Boolean = false,
    val key_color: String = "",
    val can_assign_user_flair: Boolean = false,
    val created: Double = 0.0,
    val show_media_preview: Boolean = false,
    val submission_type: String = "",
    val user_is_subscriber: Boolean = false,
    val allowed_media_in_comments: List<String>? = null,
    val allow_videogifs: Boolean = false,
    val should_archive_posts: Boolean = false,
    val user_flair_type: String? = null,
    val allow_polls: Boolean = false,
    val collapse_deleted_comments: Boolean = false,
    val emojis_custom_size: List<Int>? = null,
    val public_description_html: String? = null,
    val allow_videos: Boolean = false,
    val notification_level: String? = null,
    val should_show_media_in_comments_setting: Boolean = false,
    val can_assign_link_flair: Boolean = false,
    val accounts_active_is_fuzzed: Boolean = false,
    val allow_prediction_contributors: Boolean = false,
    val link_flair_position: String? = null,
    val user_sr_flair_enabled: Boolean? = null,
    val user_flair_enabled_in_sr: Boolean = false,
    val allow_discovery: Boolean = false,
    val accept_followers: Boolean = false,
    val user_sr_theme_enabled: Boolean = false,
    val link_flair_enabled: Boolean = false,
    val disable_contributor_requests: Boolean = false,
    val subreddit_type: String = "",
    val banner_img: String? = null,
    val show_media: Boolean = false,
    val id: String = "",
    val user_is_moderator: Boolean = false,
    val over18: Boolean = false,
    val header_title: String? = null,
    val description: String = "",
    val allow_images: Boolean = false,
    val lang: String = "",
    val url: String = "",
    val created_utc: Double = 0.0,
    val mobile_banner_image: String? = null,
    val user_is_contributor: Boolean = false,
    val allow_predictions_tournament: Boolean = false
)