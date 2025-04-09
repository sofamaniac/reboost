package com.sofamaniac.reboost.ui

import android.util.Log
import kotlinx.datetime.Instant
import java.time.Clock
import java.time.Duration
import java.util.Locale

fun formatElapsedTimeLocalized(
    creationDate: Instant,
    locale: Locale = Locale.getDefault()
): String {
    val end = Clock.systemUTC().millis()
    val duration = Duration.ofMillis(kotlin.math.abs(end - creationDate.toEpochMilliseconds()))
    Log.d("formatElapsedTimeLocalized", "${duration}, ${creationDate}, $end")

    return when {
        duration.toDays() >= 365 -> String.format(locale, "%dy", duration.toDays() / 365)
        duration.toDays() >= 30 -> String.format(locale, "%dmo", duration.toDays() / 30)
        duration.toDays() > 0 -> String.format(locale, "%dd", duration.toDays())
        duration.toHours() > 0 -> String.format(locale, "%dh", duration.toHours())
        duration.toMinutes() > 0 -> String.format(locale, "%dm", duration.toMinutes())
        else -> String.format(locale, "%ds", duration.seconds)
    }
}
