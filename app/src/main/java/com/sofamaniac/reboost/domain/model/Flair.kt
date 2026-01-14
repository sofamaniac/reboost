package com.sofamaniac.reboost.domain.model

import com.sofamaniac.reboost.data.remote.dto.LinkFlairRichtext
import kotlinx.serialization.Serializable

@Serializable
data class Flair(
    val text: String,
    val backgroundColor: String,
    val textColor: String,
    val type: String,
    val richText: List<LinkFlairRichtext>,
)