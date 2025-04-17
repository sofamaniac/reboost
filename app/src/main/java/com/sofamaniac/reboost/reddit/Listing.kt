/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.reddit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ListingData<Type>(
    val after: String? = null,
    val dist: Int? = null,
    @SerialName("modhash") val modHash: String? = null,
    val children: List<Type> = emptyList<Type>()
)
