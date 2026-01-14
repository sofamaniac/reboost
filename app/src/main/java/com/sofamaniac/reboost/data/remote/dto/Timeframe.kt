/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.dto

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