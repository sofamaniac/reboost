package com.sofamaniac.reboost.ui

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.html.HtmlPlugin


@Composable
fun SimpleMarkdown(markdown: String, modifier: Modifier = Modifier, maxLines: Int = Int.MAX_VALUE) {
    val colorScheme = MaterialTheme.colorScheme

    class MarkdownTheme : AbstractMarkwonPlugin() {
        override fun configureTheme(builder: MarkwonTheme.Builder) {
            builder
                .linkColor(colorScheme.primary.toArgb())
                .codeTextColor(colorScheme.onBackground.toArgb())
                .codeBackgroundColor(colorScheme.background.toArgb())
                .codeBlockBackgroundColor(colorScheme.background.toArgb())
                .blockQuoteColor(colorScheme.primary.toArgb())
                // Disable ruler under titles ?
                .headingBreakColor(colorScheme.background.toArgb())
        }
    }
    val context = LocalContext.current
    val markwon = Markwon.builder(context)
        .usePlugin(HtmlPlugin.create())
        .usePlugin(MarkdownTheme())
        .build()

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                setTextColor(colorScheme.onBackground.toArgb())
                setLinkTextColor(colorScheme.primary.toArgb())
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