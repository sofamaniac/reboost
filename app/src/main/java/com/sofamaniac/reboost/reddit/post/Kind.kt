package com.sofamaniac.reboost.reddit.post

import android.util.Log

enum class Kind {
    Self,
    Image,
    Video,
    Gallery,
    Meta,
    Link
}

fun getKind(post: PostDataFlat): Kind {
    val kind = if (post.is_self) Kind.Self
    else if (post.is_video) Kind.Video
    else if (post.isGallery) Kind.Gallery
    else if (post.is_meta) Kind.Meta
    else null

    if (kind != null) return kind

    return when (post.post_hint) {
        "image" -> Kind.Image
        "rich:video", "hosted:video" -> Kind.Video
        "link" -> Kind.Link
        else -> {
            Log.d("getKind", "Unknown post hint: $post")
            Kind.Self
        }
    }

}