package com.codewithrish.pdfreader.ui.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.FileProvider
import androidx.core.util.Consumer
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.model.home.Document
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import java.io.File

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

fun Context.sharePdf(
    document: Document?,
) {
    document?.let {
        val uri = FileProvider.getUriForFile(this, "${this.packageName}.provider", File(document.path))
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TITLE, document.name)
            putExtra(Intent.EXTRA_SUBJECT, document.name)
            type = "application/pdf"
        }
        this.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
    }
}

fun Context.printPdf(document: Document?) {
    document?.let {
        val file = File(document.path)
        val uri = FileProvider.getUriForFile(this, "${this.packageName}.provider", file)

        val printManager = getSystemService(Context.PRINT_SERVICE) as? android.print.PrintManager
        val jobName = "${getString(R.string.app_name)}: ${document.name}"

        val printAdapter = PdfDocumentAdapter(this, uri)

        printManager?.print(
            jobName,
            printAdapter,
            android.print.PrintAttributes.Builder().build()
        )
    }
}
