package com.sofamaniac.reboost.reddit.utils

import android.util.Log
import com.sofamaniac.reboost.reddit.post.MediaMetadata
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

object MediaMetadataSerializer : KSerializer<MediaMetadata> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("MediaMetadata", PrimitiveKind.STRING)

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: MediaMetadata) {
        MediaMetadata.serializer().serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): MediaMetadata {
        return try {
            val jsonObject = (decoder as JsonDecoder).decodeJsonElement().jsonObject
            if (jsonObject["status"]?.toString()?.replace("\"", "") == "valid") {
                val typ = jsonObject["e"]?.toString()?.replace("\"", "")
                when (typ) {
                    "Image" -> MediaMetadata.Image.serializer().deserialize(decoder)
                    "AnimatedImage" -> MediaMetadata.Gif.serializer().deserialize(decoder)
                    else -> {
                        Log.e("MediaMetadataSerializer", "Unknown type: $typ")
                        MediaMetadata.Invalid
                    }
                }
            } else MediaMetadata.Invalid
        } catch (e: Exception) {
            MediaMetadata.Invalid
        }
    }
}
