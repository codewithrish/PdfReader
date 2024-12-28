package com.codewithrish.pdfreader.core.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import com.codewithrish.pdfreader.ui.helper.PdfUtils.checkIfPdfIsPasswordProtected
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface LoadFilesRepository {
    suspend fun loadAllFilesToDatabase()
}

internal class LoadFilesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val documentsRepository: DocumentsRepository,
) : LoadFilesRepository {

    override suspend fun loadAllFilesToDatabase() {
        withContext(Dispatchers.IO) {
            getAllMediaFilesCursor()?.use { cursor ->
                // Using a map to store column indices safely
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

                // Iterate through the cursor
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
                    documentsRepository.insertDocument(document)
                }
            }
        }
    }

    @SuppressLint("Recycle")
    private suspend fun getAllMediaFilesCursor(): Cursor? {
        return withContext(Dispatchers.IO) {
            val projections = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.SIZE
            )
            val mimeTypes = DocumentType.entries.flatMap { it.mimeTypes }.filterNotNull()
            val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} IN (${mimeTypes.joinToString { "?" }})"
            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Files.getContentUri("external")
            }
            context.contentResolver.query(collection, projections, selection, mimeTypes.toTypedArray(), "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC")
        }
    }
}
