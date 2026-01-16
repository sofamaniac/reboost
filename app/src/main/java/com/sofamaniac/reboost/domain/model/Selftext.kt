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
    val markdown: String get() = selftext
    val html: String get() = selftextHtml
}