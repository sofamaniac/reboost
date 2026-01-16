package com.sofamaniac.reboost.ui.markdown

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class SpoilerSpan : ClickableSpan() {
    private var isRevealed = false

    override fun onClick(widget: View) {
        isRevealed = true
        widget.invalidate()
    }

    override fun updateDrawState(ds: TextPaint) {
        if (!isRevealed) {
            // Hide text by making it same color as background
            ds.bgColor = 0xFF2D2D2D.toInt()
            ds.color = 0xFF333333.toInt()
        } else {
            // Reveal text
            ds.bgColor = 0xFF2D2D2D.toInt()
            ds.color = Color.WHITE
        }
    }
}