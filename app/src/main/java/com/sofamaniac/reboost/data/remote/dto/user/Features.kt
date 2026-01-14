package com.sofamaniac.reboost.data.remote.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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