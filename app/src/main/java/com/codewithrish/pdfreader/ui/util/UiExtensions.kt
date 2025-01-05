package com.codewithrish.pdfreader.ui.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.core.util.Consumer
import com.codewithrish.pdfreader.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Convenience wrapper for dark mode checking
 */
val Configuration.isSystemInDarkTheme
    get() = (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

/**
 * Registers listener for configuration changes to retrieve whether system is in dark theme or not.
 * Immediately upon subscribing, it sends the current value and then registers listener for changes.
 */
fun ComponentActivity.isSystemInDarkTheme() = callbackFlow {
    channel.trySend(resources.configuration.isSystemInDarkTheme)

    val listener = Consumer<Configuration> {
        channel.trySend(it.isSystemInDarkTheme)
    }

    addOnConfigurationChangedListener(listener)

    awaitClose { removeOnConfigurationChangedListener(listener) }
}
    .distinctUntilChanged()
    .conflate()


fun Context.openPlayStoreForRating() {
    val appPackageName = packageName
    try {
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
        )
    } catch (_: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

fun Context.shareApp() {
    val context = this
    val mimeType = "text/*"
    val appPackageName = packageName
    val shareIntent = Intent().apply {
        type = mimeType
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.share_app_message) + "https://play.google.com/store/apps/details?id=$appPackageName",
        )
    }
    context.startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app_bottomsheet_title)))
}

fun Context.launchCustomTab(url: String) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}

@Composable
fun Context.ShowAppVersion(appVersion: String) {

}
