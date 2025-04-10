package com.sofamaniac.reboost.reddit

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    var url: String,
    val width: Int,
    val height: Int
) {
    init {
        url = if (url == "/self") "" else url
    }
}