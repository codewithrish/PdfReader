package com.codewithrish.pdfreader.core.analytics


data class AnalyticsEvent (
    val type: String,
    val extras: List<Param> = emptyList(),
) {

    data class Param(val key: String, val value: String)

    // Standard analytics types.
    class Types {
        companion object {
            const val SCREEN_VIEW = "screen_view" // (extras: SCREEN_NAME)
        }
    }

    // Standard parameter keys.
    class ParamKeys {
        companion object {
            const val SCREEN_NAME = "screen_name"
        }
    }
}