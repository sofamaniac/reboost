package com.sofamaniac.reboost.reddit.utils

import com.sofamaniac.reboost.reddit.post.MediaMetadata
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

object MediaMetadataOptionSerializer : KSerializer<MediaMetadata?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("MediaMetadataOption", PrimitiveKind.STRING)

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: MediaMetadata?) {
        if (value != null) MediaMetadata.serializer().serialize(encoder, value)
        else encoder.encodeNull()
    }

    override fun deserialize(decoder: Decoder): MediaMetadata? = try {
        val jsonObject = (decoder as JsonDecoder).decodeJsonElement().jsonObject
        if (jsonObject["status"]?.toString()
                ?.replace("\"", "") == "success"
        ) MediaMetadata.serializer().deserialize(decoder) else null
    } catch (e: Exception) {
        null
    }
}

object MapMediaMetadataOptionSerializer : KSerializer<Map<String, MediaMetadata?>> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("MapMediaMetadataOption", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Map<String, MediaMetadata?>) {
        MapSerializer(String.serializer(), MediaMetadataOptionSerializer).serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Map<String, MediaMetadata?> {
        return MapSerializer(
            String.serializer(),
            MediaMetadataOptionSerializer
        ).deserialize(decoder)
    }
}