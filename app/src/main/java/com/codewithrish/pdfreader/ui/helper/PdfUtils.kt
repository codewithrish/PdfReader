package com.codewithrish.pdfreader.ui.helper

import android.content.ContentResolver
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object PdfUtils {

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
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

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
                isLocked = checkIfPdfIsPasswordProtected(uri, context.contentResolver)
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

    fun checkIfPdfIsPasswordProtected(uri: Uri, contentResolver: ContentResolver): Boolean {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
            ?: return false
        return try {
            PdfRenderer(parcelFileDescriptor)
            false
        } catch (securityException: SecurityException) {
            securityException.printStackTrace()
            true
        }
    }
}
