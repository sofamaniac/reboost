package com.sofamaniac.reboost.reddit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


//@Serializable
//data class Listing<Type : Thing<Any>>(
//    val kind: String = "Listing",
//    val data: ListingData<Type> = ListingData()
//) : Iterable<Type> {
//    override fun iterator(): Iterator<Type> {
//        return data.children.iterator()
//    }
//
//    fun size(): Int {
//        return data.children.size
//    }
//
//    fun after(): String? {
//        Log.d("Listing", "after: ${data.after}")
//        return data.after
//    }
//}

@Serializable
data class ListingData<Type>(
    val after: String? = null,
    val dist: Int? = null,
    @SerialName("modhash") val modHash: String? = null,
    val children: List<Type> = emptyList<Type>()
)
