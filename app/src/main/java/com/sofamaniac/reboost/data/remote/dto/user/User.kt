package com.sofamaniac.reboost.data.remote.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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