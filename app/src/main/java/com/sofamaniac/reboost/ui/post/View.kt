package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.post.Kind
import com.sofamaniac.reboost.ui.Flair


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun PostBody(post: Post, modifier: Modifier = Modifier) {
    when (post.data.kind) {
        Kind.Image -> {
            PostImage(post)
        }

        Kind.Video -> {
            PostVideo(post)
        }

        Kind.Link -> {
            Text(
                post.data.url,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier,
            )
        }

        else -> {
            Text(
                text = post.data.selftext.annotatedString(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier,
            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostInfo(
    post: Post,
    navController: NavController,
    modifier: Modifier = Modifier,
    enablePreview: Boolean = true,
    titleClickable: Boolean = false
) {
    Row(modifier = modifier.padding(start = 2.dp)) {
        Column(
            modifier = if (enablePreview) modifier.fillMaxWidth(fraction = 0.75f) else modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                post.data.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable(
                    enabled = titleClickable,
                    onClick = {
                        Log.d("PostInfo", "PostInfo: ${post.data.permalink}")
                        navController.navigate(com.sofamaniac.reboost.Post(post.data.permalink))
                    }
                )
            )
            // TODO make clickable
            Flair(post.data.linkFlair)
            Text(post.scoreString(), style = MaterialTheme.typography.bodyMedium)
        }
        if (enablePreview) {
            // TODO make clickable
            GlideImage(
                model = post.data.thumbnail,
                contentDescription = "Thumbnail",
                modifier = modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun Post.scoreString(): AnnotatedString {
    val scoreStyle =
        MaterialTheme.typography.titleMedium.toSpanStyle()
    val score = data.score.score
    return buildAnnotatedString {
        withStyle(style = scoreStyle) {
            if (score > 10_000) {
                append((score / 1000).toString())
                append("k")
            } else {
                append(score.toString())
            }
        }
        append(" · ")
        append("${data.numComments} comments")
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun View(
    post: Post,
    navController: NavController,
    selected: MutableIntState,
    modifier: Modifier = Modifier,
    showSubredditIcon: Boolean = true,
) {
    val modifier = Modifier.padding(horizontal = 4.dp)
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp) // Space between title, content, buttons
    ) {
        PostHeader(
            post,
            navController,
            selected,
            showSubredditIcon = showSubredditIcon,
            modifier = modifier
        )
        val enablePreview = post.data.thumbnail.url.isNotEmpty() && post.data.kind == Kind.Link
        PostInfo(
            post,
            navController,
            modifier = modifier,
            enablePreview = enablePreview,
            titleClickable = true
        )
        PostBody(post)
        BottomRow(post, modifier)
    }
}