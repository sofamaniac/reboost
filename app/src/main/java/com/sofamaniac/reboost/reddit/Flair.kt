package com.sofamaniac.reboost.reddit

data class Flair(
    val text: String,
    val backgroundColor: String,
    val textColor: String,
    val type: String,
    val richText: List<LinkFlairRichtext>,
)