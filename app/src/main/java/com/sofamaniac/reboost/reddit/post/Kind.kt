package com.sofamaniac.reboost.reddit.post

import com.sofamaniac.reboost.reddit.Post

enum class Kind {
    Self,
    Image,
    Video,
    Gallery,
    Meta,
    Link
}

fun getKind(post: Post): Kind {
    val kind = if (post.data.is_self) Kind.Self
    else if (post.data.is_video) Kind.Video
    else if (post.data.isGallery) Kind.Gallery
    else if (post.data.is_meta) Kind.Meta
    else null

    if (kind != null) return kind

    return when (post.data.post_hint) {
        "image" -> Kind.Image
        "rich:video", "hosted:video" -> Kind.Video
        "link" -> Kind.Link
        else -> Kind.Self
    }

}