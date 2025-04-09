package com.sofamaniac.reboost.reddit.utils

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object InstantAsFloatSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("InstantAsFloat", PrimitiveKind.FLOAT)

    override fun deserialize(decoder: Decoder): Instant {
        val timestamp = decoder.decodeDouble() // Use decodeDouble to handle both Float & Double
        return Instant.fromEpochSeconds(timestamp.toLong())
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeDouble(value.toEpochMilliseconds().toDouble())
    }
}
