/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit.user

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import kotlinx.serialization.SerialName


interface UserAPI {

    @GET("/user/{username}/about.json")
    suspend fun getUser(@Path("username") username: String): User

}
@Serializable
data class User(
    @SerialName("accept_followers")
    val acceptFollowers: Boolean = false,
    @SerialName("awardee_karma")
    val awardeeKarma: Int = 0,
    @SerialName("awarder_karma")
    val awarderKarma: Int = 0,
    @SerialName("can_create_subreddit")
    val canCreateSubreddit: Boolean = false,
    @SerialName("can_edit_name")
    val canEditName: Boolean = false,
    @SerialName("coins")
    val coins: Int = 0,
    @SerialName("comment_karma")
    val commentKarma: Int = 0,
    @SerialName("created")
    val created: Double = 0.0,
    @SerialName("created_utc")
    val createdUtc: Double = 0.0,
    @SerialName("features")
    val features: Features = Features(),
    @SerialName("force_password_reset")
    val forcePasswordReset: Boolean = false,
    @SerialName("gold_creddits")
    val goldCreddits: Int = 0,
    @SerialName("gold_expiration")
    val goldExpiration: Float? = null,
    @SerialName("has_android_subscription")
    val hasAndroidSubscription: Boolean = false,
    @SerialName("has_external_account")
    val hasExternalAccount: Boolean = false,
    @SerialName("has_gold_subscription")
    val hasGoldSubscription: Boolean = false,
    @SerialName("has_ios_subscription")
    val hasIosSubscription: Boolean = false,
    @SerialName("has_mail")
    val hasMail: Boolean = false,
    @SerialName("has_mod_mail")
    val hasModMail: Boolean = false,
    @SerialName("has_paypal_subscription")
    val hasPaypalSubscription: Boolean = false,
    @SerialName("has_stripe_subscription")
    val hasStripeSubscription: Boolean = false,
    @SerialName("has_subscribed")
    val hasSubscribed: Boolean = false,
    @SerialName("has_subscribed_to_premium")
    val hasSubscribedToPremium: Boolean = false,
    @SerialName("has_verified_email")
    val hasVerifiedEmail: Boolean = false,
    @SerialName("has_visited_new_profile")
    val hasVisitedNewProfile: Boolean = false,
    @SerialName("hide_from_robots")
    val hideFromRobots: Boolean = false,
    @SerialName("icon_img")
    val iconImg: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("in_beta")
    val inBeta: Boolean = false,
    @SerialName("in_redesign_beta")
    val inRedesignBeta: Boolean = false,
    @SerialName("inbox_count")
    val inboxCount: Int = 0,
    @SerialName("is_blocked")
    val isBlocked: Boolean = false,
    @SerialName("is_employee")
    val isEmployee: Boolean = false,
    @SerialName("is_friend")
    val isFriend: Boolean = false,
    @SerialName("is_gold")
    val isGold: Boolean = false,
    @SerialName("is_mod")
    val isMod: Boolean = false,
    @SerialName("is_sponsor")
    val isSponsor: Boolean = false,
    @SerialName("is_suspended")
    val isSuspended: Boolean = false,
    @SerialName("link_karma")
    val linkKarma: Int = 0,
    @SerialName("modhash")
    val modhash: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("new_modmail_exists")
    val newModmailExists: Boolean? = null,
    @SerialName("num_friends")
    val numFriends: Int = 0,
    @SerialName("over_18")
    val over18: Boolean = false,
    @SerialName("password_set")
    val passwordSet: Boolean = false,
    @SerialName("pref_autoplay")
    val prefAutoplay: Boolean = false,
    @SerialName("pref_clickgadget")
    val prefClickgadget: Int = 0,
    @SerialName("pref_geopopular")
    val prefGeopopular: String = "",
    @SerialName("pref_nightmode")
    val prefNightmode: Boolean = false,
    @SerialName("pref_no_profanity")
    val prefNoProfanity: Boolean = false,
    @SerialName("pref_show_presence")
    val prefShowPresence: Boolean = false,
    @SerialName("pref_show_snoovatar")
    val prefShowSnoovatar: Boolean = false,
    @SerialName("pref_show_trending")
    val prefShowTrending: Boolean = false,
    @SerialName("pref_show_twitter")
    val prefShowTwitter: Boolean = false,
    @SerialName("pref_top_karma_subreddits")
    val prefTopKarmaSubreddits: Boolean = false,
    @SerialName("pref_video_autoplay")
    val prefVideoAutoplay: Boolean = false,
    @SerialName("snoovatar_img")
    val snoovatarImg: String = "",
    @SerialName("snoovatar_size")
    val snoovatarSize: String? = null,
    @SerialName("subreddit")
    val subreddit: Subreddit = Subreddit(),
    @SerialName("suspension_expiration_utc")
    val suspensionExpirationUtc: Float? = null,
    @SerialName("total_karma")
    val totalKarma: Int = 0,
    @SerialName("verified")
    val verified: Boolean = false
)

@Serializable
data class Features(
    @SerialName("awards_on_streams")
    val awardsOnStreams: Boolean = false,
    @SerialName("chat_group_rollout")
    val chatGroupRollout: Boolean = false,
    @SerialName("chat_subreddit")
    val chatSubreddit: Boolean = false,
    @SerialName("chat_user_settings")
    val chatUserSettings: Boolean = false,
    @SerialName("cookie_consent_banner")
    val cookieConsentBanner: Boolean = false,
    @SerialName("crowd_control_for_post")
    val crowdControlForPost: Boolean = false,
    @SerialName("do_not_track")
    val doNotTrack: Boolean = false,
    @SerialName("expensive_coins_package")
    val expensiveCoinsPackage: Boolean = false,
    @SerialName("images_in_comments")
    val imagesInComments: Boolean = false,
    @SerialName("is_email_permission_required")
    val isEmailPermissionRequired: Boolean = false,
    @SerialName("mod_awards")
    val modAwards: Boolean = false,
    @SerialName("mod_service_mute_reads")
    val modServiceMuteReads: Boolean = false,
    @SerialName("mod_service_mute_writes")
    val modServiceMuteWrites: Boolean = false,
    @SerialName("modlog_copyright_removal")
    val modlogCopyrightRemoval: Boolean = false,
    @SerialName("modmail_harassment_filter")
    val modmailHarassmentFilter: Boolean = false,
    @SerialName("mweb_xpromo_interstitial_comments_android")
    val mwebXpromoInterstitialCommentsAndroid: Boolean = false,
    @SerialName("mweb_xpromo_interstitial_comments_ios")
    val mwebXpromoInterstitialCommentsIos: Boolean = false,
    @SerialName("mweb_xpromo_modal_listing_click_daily_dismissible_android")
    val mwebXpromoModalListingClickDailyDismissibleAndroid: Boolean = false,
    @SerialName("mweb_xpromo_modal_listing_click_daily_dismissible_ios")
    val mwebXpromoModalListingClickDailyDismissibleIos: Boolean = false,
    @SerialName("noreferrer_to_noopener")
    val noreferrerToNoopener: Boolean = false,
    @SerialName("premium_subscriptions_table")
    val premiumSubscriptionsTable: Boolean = false,
    @SerialName("promoted_trend_blanks")
    val promotedTrendBlanks: Boolean = false,
    @SerialName("resized_styles_images")
    val resizedStylesImages: Boolean = false,
    @SerialName("show_amp_link")
    val showAmpLink: Boolean = false,
    @SerialName("use_pref_account_deployment")
    val usePrefAccountDeployment: Boolean = false
)

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

