/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit

import kotlinx.serialization.Serializable

@Serializable
data class AuthorInfo(
    val username: String,
    val flair: Flair,
    val authorFullname: String = "",
    val isAuthorBlocked: Boolean = false,
    val hasPatreonFlair: Boolean = false,
    val isAuthorPremium: Boolean = false,
)

val MyAuthorInfo = AuthorInfo(
    username = "me",
    flair = Flair(
        text = "",
        textColor = "",
        backgroundColor = "",
        richText = emptyList(),
        type = ""
    )
)