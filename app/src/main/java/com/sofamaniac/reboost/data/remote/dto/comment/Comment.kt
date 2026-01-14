/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.dto.comment

import com.sofamaniac.reboost.data.remote.dto.LinkFlairRichtext
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.utils.EmptyStringOrListingSerializer
import com.sofamaniac.reboost.data.remote.utils.FalseOrTimestampSerializer
import com.sofamaniac.reboost.data.remote.utils.InstantAsFloatSerializer
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
    val name: CommentFullname,
    val body: String,
    val body_html: String,
    val id: CommentId,
    val depth: Int,
    val parent_id: String,
    val permalink: String,
    @Serializable(with = EmptyStringOrListingSerializer::class)
    val replies: Thing.Listing<Thing>,

    // ================================================ //
    // AUTHOR INFORMATION
    // ================================================ //
    val author: String,
    val author_fullname: String? = null,
    val author_is_blocked: Boolean = false,
    val author_patreon_flair: Boolean = false,
    val author_premium: Boolean = false,
    // Author flair
    val author_flair_background_color: String? = null,
    val author_flair_css_class: String? = null,
    val author_flair_richtext: List<LinkFlairRichtext> = emptyList(),
    val author_flair_template_id: String? = null,
    val author_flair_text: String? = null,
    val author_flair_text_color: String? = null,
    val author_flair_type: String? = null,

    val saved: Boolean,
    val likes: Boolean? = null,
    val score: Int,
    val downs: Int,
    val ups: Int,

    val subreddit: String,
    val subreddit_id: String,
    val subreddit_name_prefixed: String,
    val subreddit_type: String,


    @Serializable(with = InstantAsFloatSerializer::class)
    val approved_at_utc: Instant? = null,
    val approved_by: String? = null,
    val archived: Boolean,
    val all_awardings: List<String> = emptyList(),
    val associated_award: String? = null,
    val awarders: List<String> = emptyList(),
    @Serializable(with = InstantAsFloatSerializer::class)
    val banned_at_utc: Instant? = null,
    val banned_by: String? = null,
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
    val distinguished: String? = null,
    @Serializable(with = FalseOrTimestampSerializer::class)
    val edited: Long? = null,
    val gilded: Int,
    // FIXME
    //val gildings: List<String>,
    val is_submitter: Boolean,
    val link_id: String,
    val locked: Boolean,
    val mod_note: String? = null,
    val mod_reason_by: String? = null,
    val mod_reason_title: String? = null,
    val mod_reports: List<String>,
    val no_follow: Boolean,
    val num_reports: Int? = null,
    val removal_reason: String? = null,
    val report_reasons: String? = null,
    val score_hidden: Boolean,
    val send_replies: Boolean,
    val stickied: Boolean,
    val top_awarded_type: String? = null,
    val total_awards_received: Int,
    val treatment_tags: List<String>,
    val unrepliable_reason: String?,
    val user_reports: List<String>
)
