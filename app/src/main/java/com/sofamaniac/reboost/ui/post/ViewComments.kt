package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.post.Kind
import org.apache.commons.text.StringEscapeUtils

@Composable
fun CommentsViewer(
    navController: NavController,
    selected: MutableIntState,
    post: Post,
    modifier: Modifier = Modifier
) {
    Scaffold { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Space between title, content, buttons
        ) {
            PostHeader(
                post,
                navController,
                selected,
                showSubredditIcon = false,
                modifier = modifier
            )
            val enablePreview = post.data.thumbnail.url.isNotEmpty()
            PostInfo(
                post,
                navController,
                modifier = modifier,
                enablePreview = enablePreview,
                titleClickable = true
            )
            BottomRow(post, modifier)
            if (post.data.kind == Kind.Self) {
                Log.d("CommentsViewer", "CommentsViewer: ${post.data.selftext.selftextHtml}")
                val html = StringEscapeUtils.unescapeHtml4(post.data.selftext.selftextHtml)
                val text = AnnotatedString.fromHtml(html)
                Text(text, modifier)
            }
        }
    }
}