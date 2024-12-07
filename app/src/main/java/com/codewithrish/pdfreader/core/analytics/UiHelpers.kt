package com.codewithrish.pdfreader.core.analytics

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    NoOpAnalyticsHelper()
}