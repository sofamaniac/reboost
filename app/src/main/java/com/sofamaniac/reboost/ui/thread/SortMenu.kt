package com.sofamaniac.reboost.ui.thread

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.sofamaniac.reboost.data.remote.dto.comment.Sort
import kotlinx.coroutines.launch

@Composable
fun SortMenu(state: ThreadViewModel) {
    val sortExpanded = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Box {
        IconButton(onClick = { sortExpanded.value = true }) {
            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
        }
        DropdownMenu(
            expanded = sortExpanded.value,
            onDismissRequest = { sortExpanded.value = false }) {
            Sort.entries.forEach { sort ->
                DropdownMenuItem(text = { Text(sort.toString()) }, onClick = {
                    state.setSort(sort)
                    scope.launch { state.refresh() }
                    sortExpanded.value = false
                })
            }
        }
    }
}