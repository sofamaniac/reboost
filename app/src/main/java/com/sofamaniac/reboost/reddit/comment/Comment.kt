/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit.comment

import com.sofamaniac.reboost.reddit.Comment
import com.sofamaniac.reboost.reddit.LinkFlairRichtext
import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.Thing
import com.sofamaniac.reboost.reddit.utils.EmptyStringOrListingSerializer
import com.sofamaniac.reboost.reddit.utils.FalseOrTimestampSerializer
import com.sofamaniac.reboost.reddit.utils.InstantAsFloatSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class CommentId(val id: String)

@Serializable
@JvmInline
value class CommentFullname(val id: String)

@Serializable
data class CommentData(
    val all_awardings: List<String> = emptyList(),
    @Serializable(with = InstantAsFloatSerializer::class)
    val approved_at_utc: Instant? = null,
    val approved_by: String? = null,
    val archived: Boolean,
    val associated_award: String? = null,
    val author: String,
    val author_flair_background_color: String? = null,
    val author_flair_css_class: String? = null,
    val author_flair_richtext: List<LinkFlairRichtext> = emptyList(),
    val author_flair_template_id: String? = null,
    val author_flair_text: String? = null,
    val author_flair_text_color: String? = null,
    val author_flair_type: String? = null,
    val author_fullname: String? = null,
    val author_is_blocked: Boolean = false,
    val author_patreon_flair: Boolean = false,
    val author_premium: Boolean = false,
    val awarders: List<String> = emptyList(),
    @Serializable(with = InstantAsFloatSerializer::class)
    val banned_at_utc: Instant? = null,
    val banned_by: String? = null,
    val body: String,
    val body_html: String,
    val can_gild: Boolean,
    val can_mod_post: Boolean,
    val collapsed: Boolean,
    val collapsed_because_crowd_control: Boolean? = null,
    val collapsed_reason: String? = null,
    // TODO Replace with enum
    val collapsed_reason_code: String? = null,
    val comment_type: String? = null,
    val controversiality: Int,
    val created: Double,
    @Serializable(with = InstantAsFloatSerializer::class)
    val created_utc: Instant,
    val depth: Int,
    val distinguished: String? = null,
    val downs: Int,
    @Serializable(with = FalseOrTimestampSerializer::class)
    val edited: Long? = null,
    val gilded: Int,
    // FIXME
    //val gildings: List<String>,
    val id: CommentId,
    val is_submitter: Boolean,
    val likes: Boolean? = null,
    val link_id: String,
    val locked: Boolean,
    val mod_note: String? = null,
    val mod_reason_by: String? = null,
    val mod_reason_title: String? = null,
    val mod_reports: List<String>,
    val name: CommentFullname,
    val no_follow: Boolean,
    val num_reports: Int? = null,
    val parent_id: String,
    val permalink: String,
    val removal_reason: String? = null,
    @Serializable(with = EmptyStringOrListingSerializer::class)
    val replies: Listing<Thing>,
    val report_reasons: String? = null,
    val saved: Boolean,
    val score: Int,
    val score_hidden: Boolean,
    val send_replies: Boolean,
    val stickied: Boolean,
    val subreddit: String,
    val subreddit_id: String,
    val subreddit_name_prefixed: String,
    val subreddit_type: String,
    val top_awarded_type: String? = null,
    val total_awards_received: Int,
    val treatment_tags: List<String>,
    val unrepliable_reason: String?,
    val ups: Int,
    val user_reports: List<String>
)
