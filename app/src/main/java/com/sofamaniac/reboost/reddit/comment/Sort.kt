package com.sofamaniac.reboost.reddit.comment

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