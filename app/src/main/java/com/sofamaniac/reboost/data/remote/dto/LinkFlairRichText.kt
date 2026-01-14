/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.dto


import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("e")
sealed class LinkFlairRichtext

@Serializable
@SerialName("text")
data class LinkFlairRichtextText(
    @SerialName("t") val text: String,
) : LinkFlairRichtext()

@Serializable
@SerialName("emoji")
data class LinkFlairRichtextEmoji(
    @SerialName("a") val emoji: String,
    @SerialName("u") val url: String,
) : LinkFlairRichtext()
