/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.utils

import com.sofamaniac.reboost.data.remote.dto.post.MediaMetadata
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object MediaMetadataSerializer : KSerializer<MediaMetadata> {
    override val descriptor: SerialDescriptor =
        //PrimitiveSerialDescriptor("MediaMetadata", PrimitiveKind.STRING)
        PolymorphicSerializer(MediaMetadata::class).descriptor

    override fun serialize(encoder: Encoder, value: MediaMetadata) {
        require(encoder is JsonEncoder)

        val element = when (value) {
            is MediaMetadata.Image -> {
                buildJsonObject {
                    put("e", JsonPrimitive("Image"))
                    encoder.json.encodeToJsonElement(MediaMetadata.Image.serializer(), value)
                        .jsonObject.forEach { (k, v) -> put(k, v) }
                }
            }
            is MediaMetadata.Gif -> {
                buildJsonObject {
                    put("e", JsonPrimitive("AnimatedImage"))
                    encoder.json.encodeToJsonElement(MediaMetadata.Gif.serializer(), value)
                        .jsonObject.forEach { (k, v) -> put(k, v) }
                }
            }
            MediaMetadata.Invalid -> {
                buildJsonObject { put("e", JsonPrimitive("Invalid")) }
            }
        }

        encoder.encodeJsonElement(element)
    }


    override fun deserialize(decoder: Decoder): MediaMetadata {
        val input = decoder as? JsonDecoder
            ?: throw SerializationException("This serializer only works with JSON")

        val element = input.decodeJsonElement()
        val obj = element.jsonObject

        if (obj["status"]?.jsonPrimitive?.content != "valid") {
            return MediaMetadata.Invalid
        }

        return when (obj["e"]?.jsonPrimitive?.content) {
            "Image" ->
                input.json.decodeFromJsonElement(
                    MediaMetadata.Image.serializer(),
                    element
                )

            "AnimatedImage" ->
                input.json.decodeFromJsonElement(
                    MediaMetadata.Gif.serializer(),
                    element
                )

            else -> MediaMetadata.Invalid
        }
    }
}
