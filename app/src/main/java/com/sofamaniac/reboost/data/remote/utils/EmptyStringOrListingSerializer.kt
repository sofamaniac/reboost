/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.utils

import android.util.Log
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Thing.Listing
import com.sofamaniac.reboost.data.remote.dto.emptyListing
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

object EmptyStringOrListingSerializer : KSerializer<Listing<Thing>> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("EmptyStringOrListing", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Listing<Thing> {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> {
                if (element.isString && element.content == "") emptyListing()
                else Listing.serializer(Thing.serializer()).deserialize(decoder)
            }

            is JsonObject -> Listing.serializer(Thing.serializer()).deserialize(decoder)
            else -> {
                Log.e("EmptyStringOrListingSerializer", "Unknown element type: $element")
                emptyListing()
            }
        }
    }

    override fun serialize(encoder: Encoder, value: Listing<Thing>) {
        if (value.isEmpty()) {
            encoder.encodeString("")
        } else {
            Listing.serializer(Thing.serializer()).serialize(encoder, value)
        }
    }
}
