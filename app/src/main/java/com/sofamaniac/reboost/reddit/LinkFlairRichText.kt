/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("e")
sealed class LinkFlairRichtext {
    @Composable
    abstract fun View()
}

@Serializable
@SerialName("text")
data class LinkFlairRichtextText(
    @SerialName("t") val text: String,
) : LinkFlairRichtext() {
    @Composable
    override fun View() {
        Text(text, style = androidx.compose.material3.MaterialTheme.typography.labelSmall)
    }
}

@Serializable
@SerialName("emoji")
data class LinkFlairRichtextEmoji(
    @SerialName("a") val emoji: String,
    @SerialName("u") val url: String,
) : LinkFlairRichtext() {
    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    override fun View() {
        GlideImage(
            model = url,
            contentDescription = emoji,
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(16.dp)
        )
    }
}
