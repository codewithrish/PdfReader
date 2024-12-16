package com.codewithrish.pdfreader.ui.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.codewithrish.pdfreader.core.model.home.Document
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

sealed class SplitPdfResult {
    data class Success(val uris: List<Uri>) : SplitPdfResult()
    data class Error(val message: String) : SplitPdfResult()
}


object PdfUtils {
    fun splitPdf(context: Context, document: Document, selectedFiles: List<Int>): SplitPdfResult {
        val pdfUris = mutableListOf<Uri>()
        return try {
            val pdDocument = PDDocument.load(File(document.path)) // Load the document

            // Filter valid page indices to avoid unnecessary errors
            val validPages = selectedFiles.filter { it in 0 until pdDocument.numberOfPages }
            if (validPages.isEmpty()) {
                return SplitPdfResult.Error("No valid pages selected for splitting.")
            }

            // Process valid pages
            validPages.forEach { pageIndex ->
                val splitDoc = PDDocument().apply { addPage(pdDocument.getPage(pageIndex)) }
                val fileName = "${document.name.removeSuffix(".pdf")}_page-${pageIndex + 1}.pdf"
                pdfUris.add(savePdfToDownloads(context, splitDoc, document.name.removeSuffix(".pdf"), fileName))
                splitDoc.close()
            }

            pdDocument.close()
            SplitPdfResult.Success(pdfUris) // Return success with URIs
        } catch (e: Exception) {
            e.printStackTrace()
            SplitPdfResult.Error("Failed to split PDF: ${e.message ?: "Unknown error"}")
        }
    }


//    fun splitPdf(context: Context, document: Document, selectedFiles: List<Int>): List<Uri> {
//        val pdfUris = mutableListOf<Uri>()
//        try {
//            val pdDocument = PDDocument.load(File(document.path))
//
//            // Filter valid page indices to avoid unnecessary checks
//            val validPages = selectedFiles.filter { it in 0 until pdDocument.numberOfPages }
//
//            // Process only valid pages
//            validPages.forEach { pageIndex ->
//                val splitDoc = PDDocument().apply { addPage(pdDocument.getPage(pageIndex)) }
//                val fileName = "${document.name.removeSuffix(".pdf")}_page-${pageIndex + 1}.pdf" // Naming starts at 1
//                pdfUris.add(savePdfToDownloads(context, splitDoc, document.name.removeSuffix(".pdf"), fileName))
//                splitDoc.close()
//            }
//
//            pdDocument.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return pdfUris
//    }

    private fun savePdfToDownloads(context: Context, document: PDDocument,folderName: String, fileName: String): Uri {
        val resolver = context.contentResolver
        val pdfUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/SplitPdfs/$folderName")
            }
            resolver.insert(MediaStore.Files.getContentUri("external"), values)
        } else {
            val fileDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SplitPdfs")
            if (!fileDir.exists()) fileDir.mkdirs()
            val outputFile = File(fileDir, fileName)
            Uri.fromFile(outputFile)
        }

        pdfUri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                document.save(outputStream)
            }
        }

        return pdfUri ?: throw IllegalStateException("Failed to save file: $fileName")
    }

    fun mergePdfs(context: Context, pdfUris: List<Uri>, outputPdfPath: String) {
        val outputDocument = PDDocument()
        try {
            // Process each input PDF and add its pages to the output document
            for (uri in pdfUris) {
                val document: PDDocument = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.openInputStream(uri)?.use { PDDocument.load(it) }
                        ?: throw IllegalStateException("Failed to open input stream")
                } else {
                    PDDocument.load(File(uri.path ?: ""))
                }
                document.use { doc ->
                    for (i in 0 until doc.numberOfPages) {
                        outputDocument.addPage(doc.getPage(i))
                    }
                }
            }
            // Save the merged PDF
            outputDocument.save(File(outputPdfPath))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputDocument.close()
        }
    }

    fun getDocumentFromUri(context: Context, uri: Uri): Document? {
        val contentResolver = context.contentResolver
        val isAboveAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        return try {
            // Default values
            var id: Long = -1L
            var path: String = ""
            var name: String = ""
            var dateTime: Long = 0L
            var mimeType: String = ""
            var size: Long = 0L
            var bookmarked = false
            var numberOfPages = 0

            // Query the content resolver
            val cursor: Cursor? = contentResolver.query(
                uri,
                null,
                null,
                null,
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    // Columns
                    val idIndex = it.getColumnIndex(MediaStore.MediaColumns._ID)
                    val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val mimeTypeIndex = it.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
                    val dateAddedIndex = it.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED)
                    val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                    val dataIndex = it.getColumnIndex(MediaStore.MediaColumns.DATA) // Deprecated in API 29+

                    // Fetch data
                    id = if (idIndex != -1) it.getLong(idIndex) else -1
                    name = if (displayNameIndex != -1) it.getString(displayNameIndex) ?: "" else ""
                    mimeType = if (mimeTypeIndex != -1) it.getString(mimeTypeIndex) ?: "" else ""
                    dateTime = if (isAboveAndroidQ && dateAddedIndex != -1) it.getLong(dateAddedIndex) * 1000 else 0L
                    size = if (sizeIndex != -1) it.getLong(sizeIndex) else 0L

                    // For Android below API 29, fetch the actual file path
                    if (!isAboveAndroidQ && dataIndex != -1) {
                        path = it.getString(dataIndex) ?: ""
                    }
                }
            }

            // For Android 10+, derive the file path (if required)
            if (isAboveAndroidQ) {
                path = uriToPath(context, uri) ?: ""
            }

            // Extract the number of pages if the file is a PDF
            if (mimeType == "application/pdf") {
                numberOfPages = getPageCountFromUri(context, uri)
            }

            // Construct the Document object
            Document(
                id = id,
                path = path,
                uri = uri.toString(),
                name = name,
                dateTime = dateTime,
                mimeType = mimeType,
                size = size,
                bookmarked = false, // Adjust if additional logic for bookmarks is required
                numberOfPages = numberOfPages,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Helper function for Android 10+ to get the file path
    private fun uriToPath(context: Context, uri: Uri): String? {
        return try {
            val fileDescriptor = context.contentResolver.openFileDescriptor(uri, "r") ?: return null
            val file = File(context.cacheDir, uri.lastPathSegment ?: "tempFile")
            FileInputStream(fileDescriptor.fileDescriptor).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getPdfPages(context: Context, uri: Uri): List<Bitmap> {
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
        return pages
    }

    fun getFirstPdfPage(context: Context, uri: Uri): Bitmap? {
        var pdfRenderer: PdfRenderer? = null
        var page: PdfRenderer.Page? = null
        var pageBitmap: Bitmap? = null

        try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
                ?: throw FileNotFoundException("File not found at $uri")
            pdfRenderer = PdfRenderer(parcelFileDescriptor)
            page = pdfRenderer.openPage(0)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pageBitmap = bitmap
            page.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            pdfRenderer?.close()
        }
        return pageBitmap
    }

    // Function to extract number of pages from PDF file
    fun getPageCountFromUri(context: Context, uri: Uri): Int {
        val fileDescriptor: ParcelFileDescriptor? = context.contentResolver.openFileDescriptor(uri, "r")
        if (fileDescriptor == null) {
            return 0
        }
        return try {
            val pdfRenderer = PdfRenderer(fileDescriptor)
            val pageCount = pdfRenderer.pageCount
            pdfRenderer.close()
            pageCount
        } catch (e: IOException) {
            e.printStackTrace()
            0
        } finally {
            fileDescriptor.close()
        }
    }
}
