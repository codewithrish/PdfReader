// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false // Hilt plugin
    alias(libs.plugins.ksp) apply false // KSP plugin
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.firebase.app.distribution) apply false
}