package com.sofamaniac.reboost.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Relationship(
    val clicked: Boolean,
    val visited: Boolean,
    val saved: Boolean,
    val liked: Boolean?
)