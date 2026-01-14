/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 11:35 PM
 *
 */

package com.sofamaniac.reboost.data.remote.utils

import com.sofamaniac.reboost.data.remote.dto.post.PostDataFlat
import com.sofamaniac.reboost.data.remote.dto.post.PostDataMapper
import com.sofamaniac.reboost.domain.model.PostData
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
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonObject -> {
                val data =
                    jsonDecoder.json.decodeFromJsonElement(PostDataFlat.serializer(), element)
                PostDataMapper.map(data)
            }

            else -> {
                throw SerializationException("Expected JsonObject")
            }
        }
    }
}