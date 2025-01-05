package com.codewithrish.pdfreader.core.ui

import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

val LocalAppLocale = staticCompositionLocalOf { Locale.getDefault() }