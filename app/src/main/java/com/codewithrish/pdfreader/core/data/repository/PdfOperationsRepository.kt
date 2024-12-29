package com.codewithrish.pdfreader.core.data.repository

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import com.tom_roush.pdfbox.pdmodel.PDDocument
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

interface PdfOperationsRepository {
    suspend fun splitPdf(document: Document, selectedPages: List<Int>): List<DocumentEntity>
    suspend fun mergePdf(selectedDocumentUris: List<String>, outputFileName: String): DocumentEntity?
}

internal class PdfOperationsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val documentsRepository: DocumentsRepository,
) : PdfOperationsRepository {
    override suspend fun splitPdf(
        document: Document,
        selectedPages: List<Int>
    ): List<DocumentEntity> {
        val documentEntities = mutableListOf<DocumentEntity>()
        try {
            withContext(Dispatchers.IO) {
                val pdDocument = PDDocument.load(File(document.path))

                val validPages = selectedPages.filter { it in 0 until pdDocument.numberOfPages }
                if (validPages.isEmpty()) {
                    throw IllegalArgumentException("No valid pages selected")
                }

                validPages.forEach { pageIndex ->
                    val splitDoc = PDDocument().apply { addPage(pdDocument.getPage(pageIndex)) }
                    val fileName = "${document.name.removeSuffix(".pdf")}_page-${pageIndex + 1}.pdf"
                    val uri = saveSplitPdfToDownloads(context, splitDoc, document.name.removeSuffix(".pdf"), fileName)
                    val cursor = getCursorFromUri(context, uri)
                    cursor?.use { cursor ->
                        val columns = listOf(
                            MediaStore.Files.FileColumns._ID,
                            MediaStore.Files.FileColumns.DATA,
                            MediaStore.Files.FileColumns.DISPLAY_NAME,
                            MediaStore.Files.FileColumns.DATE_MODIFIED,
                            MediaStore.Files.FileColumns.MIME_TYPE,
                            MediaStore.Files.FileColumns.SIZE
                        ).associateWith { cursor.getColumnIndex(it) }

                        // Ensure column indices are non-null before using them
                        val idCol = columns[MediaStore.Files.FileColumns._ID] ?: return@use
                        val pathCol = columns[MediaStore.Files.FileColumns.DATA] ?: return@use
                        val nameCol = columns[MediaStore.Files.FileColumns.DISPLAY_NAME] ?: return@use
                        val dateCol = columns[MediaStore.Files.FileColumns.DATE_MODIFIED] ?: return@use
                        val mimeTypeCol = columns[MediaStore.Files.FileColumns.MIME_TYPE] ?: return@use
                        val sizeCol = columns[MediaStore.Files.FileColumns.SIZE] ?: return@use

                        while (cursor.moveToNext()) {
                            val id = cursor.getLong(idCol)
                            val path = cursor.getStringOrNull(pathCol) ?: continue
                            val name = cursor.getStringOrNull(nameCol) ?: continue
                            val dateTime = cursor.getLongOrNull(dateCol) ?: continue
                            val mimeType = cursor.getStringOrNull(mimeTypeCol) ?: continue
                            val size = cursor.getLongOrNull(sizeCol) ?: continue
                            val contentUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id)

                            val document = DocumentEntity(
                                id = id,
                                path = path,
                                uri = contentUri.toString(),
                                name = name,
                                dateTime = dateTime,
                                mimeType = DocumentType.entries.firstOrNull { it.mimeTypes.contains(mimeType) }?.name ?: "UNKNOWN",
                                size = size,
                                bookmarked = false,
                                isLocked = false, //checkIfPdfIsPasswordProtected(contentUri, context.contentResolver)
                            )
                            documentsRepository.insertDocument(document).apply {
                                documentsRepository.getDocumentById(id).apply {
                                    this?.let { documentEntity ->
                                        documentEntities.add(documentEntity)
                                    }
                                }
                            }
                        }
                    }
                    splitDoc.close()
                }

                pdDocument.close()
                return@withContext documentEntities
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return documentEntities.toList()
    }

    override suspend fun mergePdf(selectedDocumentUris: List<String>, outputFileName: String): DocumentEntity? {
        val documentEntity: DocumentEntity? = null
        val mergedDocument = PDDocument()  // This will be your merged document

        try {
            val pdfUris = selectedDocumentUris.map { Uri.parse(it) }

            var document: PDDocument? = null

            // Process each input PDF and add its pages to the output document
            pdfUris.forEach { uri ->
                // Open the document safely depending on the API level
                document = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.openInputStream(uri)?.let { inputStream ->
                        PDDocument.load(inputStream)  // Load the document without closing it immediately
                    } ?: throw IllegalStateException("Failed to open input stream for URI: $uri")
                } else {
                    PDDocument.load(File(uri.path ?: ""))
                }

                // Add pages to the merged document
                try {
                    // Iterate through the pages of the document and add them to mergedDocument
                    for (i in 0 until document.numberOfPages) {
                        mergedDocument.addPage(document.getPage(i))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            // Save the merged PDF to the Downloads folder
            val uri = saveMergedPdfToDownloads(context, mergedDocument, outputFileName)
            val cursor = getCursorFromUri(context, uri)
            cursor?.use { cursor ->
                val columns = listOf(
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.DATE_MODIFIED,
                    MediaStore.Files.FileColumns.MIME_TYPE,
                    MediaStore.Files.FileColumns.SIZE
                ).associateWith { cursor.getColumnIndex(it) }

                // Ensure column indices are non-null before using them
                val idCol = columns[MediaStore.Files.FileColumns._ID] ?: return@use
                val pathCol = columns[MediaStore.Files.FileColumns.DATA] ?: return@use
                val nameCol = columns[MediaStore.Files.FileColumns.DISPLAY_NAME] ?: return@use
                val dateCol = columns[MediaStore.Files.FileColumns.DATE_MODIFIED] ?: return@use
                val mimeTypeCol = columns[MediaStore.Files.FileColumns.MIME_TYPE] ?: return@use
                val sizeCol = columns[MediaStore.Files.FileColumns.SIZE] ?: return@use

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val path = cursor.getStringOrNull(pathCol) ?: continue
                    val name = cursor.getStringOrNull(nameCol) ?: continue
                    val dateTime = cursor.getLongOrNull(dateCol) ?: continue
                    val mimeType = cursor.getStringOrNull(mimeTypeCol) ?: continue
                    val size = cursor.getLongOrNull(sizeCol) ?: continue
                    val contentUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id)

                    val document = DocumentEntity(
                        id = id,
                        path = path,
                        uri = contentUri.toString(),
                        name = name,
                        dateTime = dateTime,
                        mimeType = DocumentType.entries.firstOrNull { it.mimeTypes.contains(mimeType) }?.name ?: "UNKNOWN",
                        size = size,
                        bookmarked = false,
                        isLocked = false, //checkIfPdfIsPasswordProtected(contentUri, context.contentResolver)
                    )
                    documentsRepository.insertDocument(document).apply {
                        documentsRepository.getDocumentById(id).apply {
                            this?.let { documentEntity ->
                                return documentEntity
                            }
                        }
                    }
                }
            }

            // Close the document after pages have been added to mergedDocument
            document?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mergedDocument.close()  // Close the merged document after saving
        }
        return documentEntity
    }


    private fun getCursorFromUri(context: Context, uri: Uri): Cursor? {
        return try {
            // Query the content resolver for the Uri
            context.contentResolver.query(
                uri,
                null, // Projection (columns to retrieve), null retrieves all columns
                null, // Selection (filter criteria), null retrieves all rows
                null, // Selection arguments
                null  // Sort order
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null in case of an exception
        }
    }

    private fun saveSplitPdfToDownloads(context: Context, document: PDDocument, folderName: String, fileName: String): Uri {
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

    private fun saveMergedPdfToDownloads(context: Context, document: PDDocument, fileName: String): Uri {
        val resolver = context.contentResolver
        val pdfUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/MergedPdfs")
            }
            resolver.insert(MediaStore.Files.getContentUri("external"), values)
        } else {
            val fileDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "MergedPdfs")
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
}
