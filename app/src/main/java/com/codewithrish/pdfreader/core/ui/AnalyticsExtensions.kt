package com.codewithrish.pdfreader.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.codewithrish.pdfreader.core.analytics.AnalyticsEvent
import com.codewithrish.pdfreader.core.analytics.AnalyticsEvent.Param
import com.codewithrish.pdfreader.core.analytics.AnalyticsEvent.ParamKeys
import com.codewithrish.pdfreader.core.analytics.AnalyticsEvent.Types
import com.codewithrish.pdfreader.core.analytics.AnalyticsHelper
import com.codewithrish.pdfreader.core.analytics.LocalAnalyticsHelper

fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        AnalyticsEvent(
            type = Types.SCREEN_VIEW,
            extras = listOf(
                Param(ParamKeys.SCREEN_NAME, screenName)
            )
        )
    )
}

/**
 * A side-effect which records a screen view event.
 */
@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose {}
}