package com.sofamaniac.reboost.data.remote.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Subreddit(
    @SerialName("accept_followers")
    val acceptFollowers: Boolean = false,
    @SerialName("allowed_media_in_comments")
    val allowedMediaInComments: List<Boolean?> = listOf(),
    @SerialName("banner_img")
    val bannerImg: String = "",
    @SerialName("banner_size")
    val bannerSize: String? = null,
    @SerialName("coins")
    val coins: Int = 0,
    @SerialName("community_icon")
    val communityIcon: String? = null,
    @SerialName("default_set")
    val defaultSet: Boolean = false,
    @SerialName("description")
    val description: String = "",
    @SerialName("disable_contributor_requests")
    val disableContributorRequests: Boolean = false,
    @SerialName("display_name")
    val displayName: String = "",
    @SerialName("display_name_prefixed")
    val displayNamePrefixed: String = "",
    @SerialName("free_form_reports")
    val freeFormReports: Boolean = false,
    @SerialName("header_img")
    val headerImg: String? = null,
    @SerialName("header_size")
    val headerSize: String? = null,
    @SerialName("icon_color")
    val iconColor: String = "",
    @SerialName("icon_img")
    val iconImg: String = "",
    @SerialName("icon_size")
    val iconSize: List<Int> = listOf(),
    @SerialName("is_default_banner")
    val isDefaultBanner: Boolean = false,
    @SerialName("is_default_icon")
    val isDefaultIcon: Boolean = false,
    @SerialName("key_color")
    val keyColor: String = "",
    @SerialName("link_flair_enabled")
    val linkFlairEnabled: Boolean = false,
    @SerialName("link_flair_position")
    val linkFlairPosition: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("over_18")
    val over18: Boolean = false,
    @SerialName("previous_names")
    val previousNames: List<String?> = listOf(),
    @SerialName("primary_color")
    val primaryColor: String = "",
    @SerialName("public_description")
    val publicDescription: String = "",
    @SerialName("quarantine")
    val quarantine: Boolean = false,
    @SerialName("restrict_commenting")
    val restrictCommenting: Boolean = false,
    @SerialName("restrict_posting")
    val restrictPosting: Boolean = false,
    @SerialName("show_media")
    val showMedia: Boolean = false,
    @SerialName("submit_link_label")
    val submitLinkLabel: String = "",
    @SerialName("submit_text_label")
    val submitTextLabel: String = "",
    @SerialName("subreddit_type")
    val subredditType: String = "",
    @SerialName("subscribers")
    val subscribers: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("user_is_banned")
    val userIsBanned: Boolean = false,
    @SerialName("user_is_contributor")
    val userIsContributor: Boolean = false,
    @SerialName("user_is_moderator")
    val userIsModerator: Boolean = false,
    @SerialName("user_is_muted")
    val userIsMuted: Boolean? = null,
    @SerialName("user_is_subscriber")
    val userIsSubscriber: Boolean = false
)