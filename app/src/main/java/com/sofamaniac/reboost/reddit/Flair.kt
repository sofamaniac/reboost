package com.sofamaniac.reboost.reddit

import kotlinx.serialization.Serializable

@Serializable
data class Flair(
    val text: String,
    val backgroundColor: String,
    val textColor: String,
    val type: String,
    val richText: List<LinkFlairRichtext>,
)