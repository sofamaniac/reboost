package com.sofamaniac.reboost.data.remote.dto.post

enum class Sort {
    Hot,
    Best,
    New,
    Rising,
    Top,
    Controversial;

    override fun toString(): String {
        return super.toString().lowercase()
    }

    fun isTimeframe(): Boolean {
        return when (this) {
            Hot, Best, New, Rising -> false
            Top, Controversial -> true
        }
    }
}