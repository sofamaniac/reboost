/*
 * Copyright (c) 2025 Antoine Grimod
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    id("com.jaredsburrows.license") version "0.9.8" apply false
}


tasks.updateDaemonJvm {
    jvmVersion = JavaLanguageVersion.of(21)
}


