package com.sofamaniac.reboost.ui

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon

@Composable
fun SimpleMarkdown(markdown: String, modifier: Modifier = Modifier, maxLines: Int = Int.MAX_VALUE) {
    val context = LocalContext.current
    val markwon = Markwon.create(context)

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
                this.maxLines = maxLines
            }
        },
        modifier = modifier,
        update = { textView ->
            markwon.setMarkdown(textView, markdown)
        }
    )
}