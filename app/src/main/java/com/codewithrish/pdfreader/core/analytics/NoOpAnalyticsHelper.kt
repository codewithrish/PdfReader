package com.codewithrish.pdfreader.core.analytics

class NoOpAnalyticsHelper : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) = Unit
}
