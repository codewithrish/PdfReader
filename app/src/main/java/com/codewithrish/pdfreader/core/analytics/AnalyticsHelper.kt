package com.codewithrish.pdfreader.core.analytics

import android.os.Bundle
import com.codewithrish.pdfreader.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.mixpanel.android.mpmetrics.MixpanelAPI
import timber.log.Timber
import javax.inject.Inject

interface AnalyticsHelper {
    fun logEvent(event: AnalyticsEvent)
}

internal class AnalyticsHelperImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val mixpanelAPI: MixpanelAPI
) : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        Timber.tag("Analytics").d("Logging event: $event")
        if (!BuildConfig.DEBUG) {
            // Firebase & MixPanel
            val firebaseParams = Bundle()
            val mixPanelParams = mutableMapOf<String, Any>()
            event.extras.forEach { (key, value) ->
                firebaseParams.putString(key, value)
                mixPanelParams.put(key, value)
            }
            firebaseAnalytics.logEvent(event.type, firebaseParams)
            mixpanelAPI.trackMap(event.type, mixPanelParams)
        }
    }
}