package com.sofamaniac.reboost.reddit.post

import kotlinx.serialization.Serializable

@Serializable
enum class Timeframe {
    Hour,
    Day,
    Week,
    Month,
    Year,
    All;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}