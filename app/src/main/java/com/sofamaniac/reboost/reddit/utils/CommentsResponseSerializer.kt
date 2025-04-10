package com.sofamaniac.reboost.reddit.utils

import com.sofamaniac.reboost.reddit.Comment
import com.sofamaniac.reboost.reddit.CommentsResponse
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.ListingData
import com.sofamaniac.reboost.reddit.More
import com.sofamaniac.reboost.reddit.Thing
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

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
        val element = jsonDecoder.decodeJsonElement()
        return when (element) {
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

                    val (commentListingFinal, more) = if (commentListing.data.children.last() is More) {
                        val last = commentListing.data.children.last() as More
                        val data = ListingData(
                            commentListing.data.after,
                            commentListing.data.dist,
                            commentListing.data.modHash,
                            commentListing.data.children.dropLast(1)
                        )
                        Pair(Listing<Comment>(data as ListingData<Comment>), last)
                    } else {
                        Pair(commentListing as Listing<Comment>, null)
                    }

                    CommentsResponse(postListing, commentListingFinal, more)
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