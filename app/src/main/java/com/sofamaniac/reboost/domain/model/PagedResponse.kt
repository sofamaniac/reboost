package com.sofamaniac.reboost.domain.model

import com.sofamaniac.reboost.data.remote.dto.Thing

data class PagedResponse<T : Thing>(
    val data: List<T> = emptyList<T>(),
    val after: String? = null,
    val total: Int = 0
)