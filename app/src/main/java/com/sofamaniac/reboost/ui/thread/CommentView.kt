package com.sofamaniac.reboost.ui.thread

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.data.remote.dto.Thing

@Composable
fun CommentView(comment: Thing.Comment, modifier: Modifier = Modifier) {
    val comments = flattenComments(comment)
    Column(modifier = Modifier.fillMaxWidth()) {
        for (comment in comments) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(comment.second) {
                    VerticalDivider(
                        modifier = Modifier
                            .width(2.dp)
                            .fillMaxHeight()
                    )

                }
                when (comment.first) {
                    is Thing.Comment -> CommentNode(comment.first as Thing.Comment)
                    is Thing.More -> MoreViewer(comment.first as Thing.More)
                    else -> {
                        Log.e(
                            "CommentViewImp",
                            "Unknown comment type: ${comment.first.javaClass.name}"
                        )
                    }
                }
            }
        }
        HorizontalDivider()
    }
}
fun flattenComments(comment: Thing, depth: Int = 0): List<Pair<Thing, Int>> {
    return when (comment) {
        is Thing.Comment -> {
            listOf(Pair(comment, depth)) + comment.data.replies.flatMap {
                flattenComments(
                    it,
                    depth + 1
                )
            }
        }

        is Thing.More -> {
            listOf(Pair(comment, depth))
        }

        else -> {
            Log.e("flattenComments", "Unknown comment type: ${comment.javaClass.name}")
            emptyList()
        }

    }
}
