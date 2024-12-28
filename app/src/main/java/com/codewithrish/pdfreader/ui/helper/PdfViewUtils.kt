package com.codewithrish.pdfreader.ui.helper

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.PDFRenderer
import java.io.File
import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

suspend fun generatePdfThumbnail(
    pdfFile: File,
    thumbnailWidth: Int = 200,
    thumbnailHeight: Int = 300
): ImageBitmap? = withContext(Dispatchers.IO) {
    try {
        val document = PDDocument.load(pdfFile)
        val pdfRenderer = PDFRenderer(document)
        val pageBitmap = pdfRenderer.renderImageWithDPI(0, 300f)
        document.close()

        Bitmap.createScaledBitmap(
            pageBitmap,
            thumbnailWidth,
            thumbnailHeight,
            true
        ).asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


suspend fun getPdfPagesIo(context: Context, uri: Uri): List<Bitmap> = withContext(Dispatchers.IO) {
    val pages = mutableListOf<Bitmap>()
    var pdfRenderer: PdfRenderer? = null
    var page: PdfRenderer.Page? = null

    try {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
            ?: throw FileNotFoundException("File not found at $uri")
        pdfRenderer = PdfRenderer(parcelFileDescriptor)
        for (i in 0 until pdfRenderer.pageCount) {
            page = pdfRenderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pages.add(bitmap)
            page.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        pdfRenderer?.close()
    }

    return@withContext pages
}
