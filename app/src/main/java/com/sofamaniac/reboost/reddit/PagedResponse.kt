package com.sofamaniac.reboost.reddit

data class PagedResponse<T : Thing>(
    val data: List<T> = emptyList<T>(),
    val after: String? = null,
    val total: Int = 0
)
