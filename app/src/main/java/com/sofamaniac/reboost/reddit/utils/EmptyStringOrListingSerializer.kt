package com.sofamaniac.reboost.reddit.utils

import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.Thing
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive

object EmptyStringOrListingSerializer : KSerializer<Listing<Thing>?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("EmptyStringOrListing", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Listing<Thing>? {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        val element = jsonDecoder.decodeJsonElement()
        return when (element) {
            is JsonPrimitive -> {
                if (element.isString && element.content == "") null
                else Listing.serializer(Thing.serializer()).deserialize(decoder)
            }

            else -> null
        }
    }

    override fun serialize(encoder: Encoder, value: Listing<Thing>?) {
        if (value == null) {
            encoder.encodeString("")
        } else {
            Listing.serializer(Thing.serializer()).serialize(encoder, value)
        }
    }
}
