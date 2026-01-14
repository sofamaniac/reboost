/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.dto.comment

enum class Sort {
    Confidence,
    Top,
    Best,
    New,
    Controversial,
    Old,
    Random,
    Qa,
    Live;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}