package com.codewithrish.pdfreader.ui.util

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import java.io.FileInputStream
import java.io.FileOutputStream

class PdfDocumentAdapter(
    private val context: Context,
    private val uri: Uri
) : PrintDocumentAdapter() {

    override fun onStart() {
        // Initialize resources if needed
    }

    override fun onLayout(
        oldAttributes: android.print.PrintAttributes?,
        newAttributes: android.print.PrintAttributes?,
        cancellationSignal: android.os.CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: android.os.Bundle?
    ) {
        val info = PrintDocumentInfo.Builder(uri.lastPathSegment ?: "PDF")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
            .build()
        callback?.onLayoutFinished(info, true)
    }

    override fun onWrite(
        pages: Array<out android.print.PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: android.os.CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        try {
            FileInputStream(context.contentResolver.openFileDescriptor(uri, "r")!!.fileDescriptor).use { input ->
                FileOutputStream(destination!!.fileDescriptor).use { output ->
                    input.copyTo(output)
                }
            }
            callback?.onWriteFinished(arrayOf(android.print.PageRange.ALL_PAGES))
        } catch (e: Exception) {
            callback?.onWriteFailed(e.message)
        }
    }

    override fun onFinish() {
        // Clean up resources if needed
    }
}
