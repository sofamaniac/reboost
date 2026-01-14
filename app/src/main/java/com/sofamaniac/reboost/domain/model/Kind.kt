/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.model

import android.util.Log
import com.sofamaniac.reboost.data.remote.dto.post.PostDataFlat

enum class Kind {
    Self,
    Image,
    Video,
    Gallery,
    Meta,
    Link
}

fun getKind(post: PostDataFlat): Kind {
    val kind = if (post.isSelfPost) Kind.Self
    else if (post.isVideo) Kind.Video
    else if (post.isGallery || post.galleryData != null) Kind.Gallery
    else if (post.isMeta) Kind.Meta
    else null

    if (kind != null) return kind

    return when (post.postHint) {
        "image" -> Kind.Image
        "rich:video", "hosted:video" -> Kind.Video
        "link" -> Kind.Link
        else -> {
            Log.d("getKind", "Unknown post hint: $post")
            Kind.Self
        }
    }

}