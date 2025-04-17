/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun LicenseWebView() {
    AndroidView(factory = { context ->
        WebView(context).apply {
            loadUrl("file:///android_asset/open_source_licenses.html")
        }
    })
}