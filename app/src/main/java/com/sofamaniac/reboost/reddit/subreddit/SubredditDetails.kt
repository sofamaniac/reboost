/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:33 PM
 *
 */

package com.sofamaniac.reboost.reddit.subreddit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubredditDetails(
    @SerialName("default_set")
    val defaultSet: Boolean,
    @SerialName("banner_img")
    val bannerImg: String,
    @SerialName("allowed_media_in_comments")
    val allowedMediaInComments: List<String>,
    @SerialName("user_is_banned")
    val userIsBanned: Boolean,
    @SerialName("free_form_reports")
    val freeFormReports: Boolean,
    @SerialName("community_icon")
    val communityIcon: String?,
    @SerialName("show_media")
    val showMedia: Boolean,
    val description: String,
    @SerialName("user_is_muted")
    val userIsMuted: Boolean?,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("header_img")
    val headerImg: String?,
    val title: String,
    @SerialName("previous_names")
    val previousNames: List<String>,
    @SerialName("user_is_moderator")
    val userIsModerator: Boolean,
    @SerialName("over_18")
    val over18: Boolean,
    @SerialName("icon_size")
    val iconSize: List<Int>?,
    @SerialName("primary_color")
    val primaryColor: String?,
    @SerialName("icon_img")
    val iconImg: String?,
    @SerialName("icon_color")
    val iconColor: String,
    @SerialName("submit_link_label")
    val submitLinkLabel: String,
    @SerialName("header_size")
    val headerSize: List<Int>?,
    @SerialName("restrict_posting")
    val restrictPosting: Boolean,
    @SerialName("restrict_commenting")
    val restrictCommenting: Boolean,
    val subscribers: Int,
    @SerialName("submit_text_label")
    val submitTextLabel: String,
    @SerialName("link_flair_position")
    val linkFlairPosition: String,
    @SerialName("display_name_prefixed")
    val displayNamePrefixed: String,
    @SerialName("key_color")
    val keyColor: String?,
    val name: String,
    val created: Double,
    val url: String,
    val quarantine: Boolean,
    @SerialName("created_utc")
    val createdUtc: Double,
    @SerialName("banner_size")
    val bannerSize: List<Int>?,
    @SerialName("user_is_contributor")
    val userIsContributor: Boolean,
    @SerialName("accept_followers")
    val acceptFollowers: Boolean,
    @SerialName("public_description")
    val publicDescription: String,
    @SerialName("link_flair_enabled")
    val linkFlairEnabled: Boolean,
    @SerialName("disable_contributor_requests")
    val disableContributorRequests: Boolean,
    @SerialName("subreddit_type")
    val subredditType: String,
    @SerialName("user_is_subscriber")
    val userIsSubscriber: Boolean
) {
    val icon: SubredditIcon
        get() {
            return when {
                communityIcon != null -> SubredditIcon.Icon(communityIcon)
                iconImg != null -> SubredditIcon.Icon(iconImg)
                keyColor != null -> SubredditIcon.Color(keyColor)
                primaryColor != null -> SubredditIcon.Color(primaryColor)
                else -> SubredditIcon.Color("black")
            }
        }
}

sealed class SubredditIcon {
    data class Icon(val url: String) : SubredditIcon()
    data class Color(val color: String) : SubredditIcon()
}