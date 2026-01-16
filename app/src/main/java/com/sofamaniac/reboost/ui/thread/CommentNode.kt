package com.sofamaniac.reboost.ui.thread

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.ui.markdown.SimpleMarkdown

@Composable
fun CommentNode(comment: Thing.Comment, modifier: Modifier = Modifier) {
    Column {
        Text(comment.data.author, color = MaterialTheme.colorScheme.primary)
        SimpleMarkdown(
            comment.data.body,
            modifier = modifier,
        )
    }
}
