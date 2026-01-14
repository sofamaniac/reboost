package com.sofamaniac.reboost.domain.model

import com.sofamaniac.reboost.domain.model.Flair
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