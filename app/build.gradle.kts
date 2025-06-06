/*
 * Copyright (c) 2025 Antoine Grimod
 */

import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.jaredsburrows.license")
}

android {
    namespace = "com.sofamaniac.reboost"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.sofamaniac.reboost"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["appAuthRedirectScheme"] = "com.sofamaniac.reboost"

        val keystoreFile = project.rootProject.file("apikeys.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val apiKey = properties.getProperty("REDDIT_CLIENT_ID")!!

        buildConfigField(
            type = "String",
            name = "REDDIT_CLIENT_ID",
            value = apiKey
        )

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

}


// Avoid duplicate annotations https://stackoverflow.com/a/58909363
configurations {
    all {
        exclude(group = "org.jetbrains", module = "annotations-java5")
    }
}


licenseReport {
    // Generate reports
    generateCsvReport = false
    generateHtmlReport = true
    generateJsonReport = false
    generateTextReport = false

    // Copy reports - These options are ignored for Java projects
    copyCsvReportToAssets = false
    copyHtmlReportToAssets = true
    copyJsonReportToAssets = false
    copyTextReportToAssets = false
    useVariantSpecificAssetDirs = false

    // Ignore licenses for certain artifact patterns
    //ignoredPatterns = emptyList()

    // Show versions in the report - default is false
    showVersions = true
}


dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.appauth)
    implementation(libs.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor) // Optional, for request/response logging
    // Json serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.paging.compose)

    implementation(libs.glide) //check for latest version
    implementation(libs.compose) // check for latest version
    annotationProcessor(libs.compiler) //check for latest version

    // Parse HTML-encoded urls
    implementation(libs.commons.text)

    // More material icons
    implementation(libs.androidx.material.icons.extended)

    // Video player
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    // Rooms
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp(libs.androidx.room.compiler)

    // Render markdown
    implementation(libs.markwon.core) // Markwon core
    implementation(libs.markwon.html) // Optional: HTML support
    implementation(libs.markwon.image.glide) // Optional: Image support
    implementation(libs.markwon.linkify) // Optional: Link support
    implementation(libs.markwon.syntax.highlight) // Optional: Syntax highlighting (code blocks)
    implementation(libs.markwon.tables)
    implementation(libs.markwon.tasklist)
    implementation(libs.markwon.strikethrough)


    //implementation("io.noties.markwon:recycler:4.6.2") // Optional: If using RecyclerView
    //implementation("org.commonmark:commonmark:0.21.0") // Required for syntax highlighting
    //implementation("org.commonmark:commonmark-ext-gfm-tables:0.21.0") // Optional: Support for tables
    //implementation("org.commonmark:commonmark-ext-heading-anchor:0.21.0") // Optional: Support for heading anchor
}
