/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit.utils

import android.util.Log
import com.sofamaniac.reboost.reddit.post.RedditVideo
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

object TranscodedVideo : KSerializer<RedditVideo?> {
    override val descriptor: SerialDescriptor =
        // TODO find better descriptor
        PrimitiveSerialDescriptor("TranscodedVideo", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: RedditVideo?) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): RedditVideo? {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        val element = jsonDecoder.decodeJsonElement()
        return when (element) {
            is JsonObject -> {
                if (element.jsonObject["transcoding_status"]?.toString() == "completed") {
                    jsonDecoder.json.decodeFromJsonElement(RedditVideo.serializer(), element)
                } else {
                    null
                }
            }

            else -> {
                Log.e("TranscodedMedia", "Expected JsonObject but got: $element")
                null
            }
        }

    }
}