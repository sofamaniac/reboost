/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.utils

import com.sofamaniac.reboost.data.remote.api.CommentsResponse
import com.sofamaniac.reboost.data.remote.dto.Thing.Listing
import com.sofamaniac.reboost.data.remote.dto.Thing.More
import com.sofamaniac.reboost.data.remote.dto.Thing.Post
import com.sofamaniac.reboost.data.remote.dto.Thing
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder

object CommentsResponseSerializer : KSerializer<CommentsResponse> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("CommentsResponse", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: CommentsResponse
    ) {
        TODO("Not yet implemented")
    }

    override fun deserialize(decoder: Decoder): CommentsResponse {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonArray -> {
                if (element.size == 2) {
                    val postListing = jsonDecoder.json.decodeFromJsonElement(
                        Listing.serializer(Post.serializer()),
                        element[0]
                    )
                    val commentListing = jsonDecoder.json.decodeFromJsonElement(
                        Listing.serializer(Thing.serializer()),
                        element[1]
                    )

                    val more = if (commentListing.data.children.lastOrNull() is More) {
                        val last = commentListing.data.children.last() as More
                        last
                    } else {
                        null
                    }

                    CommentsResponse(postListing, commentListing, more)
                } else {
                    throw SerializationException("Expected 2 elements in the array")
                }
            }

            else -> {
                throw SerializationException("Expected array")
            }
        }
    }
}