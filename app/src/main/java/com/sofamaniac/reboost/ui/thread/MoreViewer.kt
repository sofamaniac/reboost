package com.sofamaniac.reboost.ui.thread

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.data.remote.dto.Thing

@Composable
fun MoreViewer(commentsResponse: Thing.More, modifier: Modifier = Modifier.Companion) {
    Text("More", modifier, style = MaterialTheme.typography.titleSmall)
}