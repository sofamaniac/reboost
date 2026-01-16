package com.sofamaniac.reboost.ui.markdown

import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.inlineparser.InlineProcessor
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import org.commonmark.node.CustomBlock
import org.commonmark.node.CustomNode
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import java.util.regex.Pattern

class SpoilerBlock() : CustomBlock()

class SpoilerInline(val content: String) : CustomNode()

class SpoilerInlineProcessor : InlineProcessor() {

    private val pattern = Pattern.compile("¤¤(.*?)¤¤", Pattern.DOTALL)

    override fun specialCharacter(): Char {
        return '¤'
    }

    override fun parse(): Node? {
        val spoiler = match(pattern)
        if (spoiler == null) {
            return null
        } else {
            val content = spoiler.substring(2, spoiler.length - 2)
            val spoilerNode = SpoilerInline(content)
            return spoilerNode
        }
    }

}


/**
 * Markwon plugin for Reddit spoilers
 */
class RedditSpoilerPlugin : AbstractMarkwonPlugin() {

    override fun configureParser(builder: Parser.Builder) {
        // Add block parser factory
        //builder.customBlockParserFactory(SpoilerBlockParserFactory())
    }

    override fun configure(registry: MarkwonPlugin.Registry) {
        registry.require(MarkwonInlineParserPlugin::class.java)
            .factoryBuilder().addInlineProcessor(SpoilerInlineProcessor())
    }

    override fun configureVisitor(builder: MarkwonVisitor.Builder) {
        builder.on(SpoilerBlock::class.java) { visitor, spoilerBlock ->
            // Visit children (the text content)
            val length = visitor.length()
            visitor.visitChildren(spoilerBlock)

            // Apply spoiler span to the rendered text
            visitor.setSpans(length, SpoilerSpan())
        }

        builder.on(SpoilerInline::class.java) { visitor, spoilerInline ->
            val length = visitor.length()
            visitor.builder().append(spoilerInline.content)
            visitor.setSpans(length, SpoilerSpan())
        }
    }

    companion object {
        fun create(): RedditSpoilerPlugin {
            return RedditSpoilerPlugin()
        }
    }
}

// Extension function for easier usage
fun Markwon.Builder.useRedditSpoilers(): Markwon.Builder {
    //return this.usePlugin(RedditSpoilerSpanFactoryPlugin())
    return this.usePlugin(RedditSpoilerPlugin.create())
}