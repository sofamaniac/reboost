package com.sofamaniac.reboost.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.sofamaniac.reboost.reddit.Flair
import com.sofamaniac.reboost.reddit.LinkFlairRichtext


@Composable
fun FlairRichtext(richText: List<LinkFlairRichtext>) {
    for (i in richText) {
        i.View()
    }
}

fun mapColor(color: String): Color {
    if (color.startsWith("#")) {
        if (color.length != 7) {
            return Color.Black
        }
        return Color(
            red = color.substring(1, 3).toInt(16),
            green = color.substring(3, 5).toInt(16),
            blue = color.substring(5, 7).toInt(16)
        )
    } else if (color.isNotEmpty()) {
        Log.d("Flair", "named color: $color")
        return when (color) {
            "light" -> Color.White
            "dark" -> Color.DarkGray
            else -> Color(color.toColorInt())
        }
    } else {
        return Color.Gray
    }
}


@Composable
fun Flair(flair: Flair) {
    if (flair.text.isEmpty() && flair.richText.isEmpty()) return
    val backgroundColor = mapColor(flair.backgroundColor)
    val textColor = mapColor(flair.textColor)
    Surface(color = backgroundColor, shape = RoundedCornerShape(corner = CornerSize(2.dp))) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            when (flair.type) {
                "richtext" -> {
                    if (flair.richText.isNotEmpty()) {
                        FlairRichtext(flair.richText)
                    } else {
                        Text(
                            text = flair.text,
                            color = textColor,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                "text" -> {
                    Text(
                        text = flair.text,
                        color = textColor,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}