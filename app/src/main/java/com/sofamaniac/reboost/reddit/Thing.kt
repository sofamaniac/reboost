package com.sofamaniac.reboost.reddit

import kotlinx.serialization.Serializable

@Serializable
abstract class Thing<out T>(private val kind: String) {
    abstract val data: T
    abstract fun fullname(): String
    abstract fun id(): String
}
