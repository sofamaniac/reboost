package com.sofamaniac.reboost.reddit.post

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import kotlinx.serialization.Serializable
import org.apache.commons.lang3.StringUtils
import org.apache.commons.text.StringEscapeUtils

@Serializable
data class Selftext(
    private val selftext: String,
    private val selftextHtml: String
) {
    fun html(): String {
        return StringEscapeUtils.unescapeHtml4(selftextHtml)
    }
}