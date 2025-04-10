package com.sofamaniac.reboost.reddit.utils

import com.sofamaniac.reboost.reddit.post.PostData
import com.sofamaniac.reboost.reddit.post.PostDataFlat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject

object PostDataSerializer : KSerializer<PostData> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("PostData", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: PostData
    ) {
        PostData.serializer().serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): PostData {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        val element = jsonDecoder.decodeJsonElement()
        return when (element) {
            is JsonObject -> {
                jsonDecoder.json.decodeFromJsonElement(PostDataFlat.serializer(), element)
                    .toPostData()
            }

            else -> {
                throw SerializationException("Expected JsonObject")
            }
        }
    }
}