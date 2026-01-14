/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Selftext(
    private val selftext: String,
    private val selftextHtml: String
) {
    fun html(): String {
        //return StringEscapeUtils.unescapeHtml4(selftextHtml)
        return selftextHtml
    }
}