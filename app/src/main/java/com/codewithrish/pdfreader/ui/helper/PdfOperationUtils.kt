package com.codewithrish.pdfreader.ui.helper

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.codewithrish.pdfreader.core.model.home.Document
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission
import com.tom_roush.pdfbox.pdmodel.encryption.StandardProtectionPolicy
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class PdfOperationState<out R> {
    data object Idle : PdfOperationState<Nothing>()
    data object Loading : PdfOperationState<Nothing>()
    data class Error(val error: String, val errorCode: Int? = null) : PdfOperationState<Nothing>()
    data class Success<out T>(val data: T) : PdfOperationState<T>()
}

fun unlockPdf(inputPath: String, outputPath: String, password: String) {
    try {
        val pdfFile = File(inputPath)
        val outputFile = File(outputPath)

        // Load the password-protected PDF
        val document = PDDocument.load(pdfFile, password)

        // Remove security
        document.isAllSecurityToBeRemoved = true

        // Save the unlocked PDF
        FileOutputStream(outputFile).use { outputStream ->
            document.save(outputStream)
        }

        document.close()
        println("PDF unlocked successfully: $outputPath")
    } catch (e: Exception) {
        e.printStackTrace()
        println("Failed to unlock PDF: ${e.message}")
    }
}

fun lockPdf(inputPath: String, outputPath: String, userPassword: String, ownerPassword: String) {
    try {
        // Load the PDF document
        val pdfFile = File(inputPath)
        val document = PDDocument.load(pdfFile)

        // Set permissions (optional)
        val accessPermission = AccessPermission()
        accessPermission.setCanPrint(false)
        accessPermission.setCanModify(false)

        // Create a protection policy
        val protectionPolicy = StandardProtectionPolicy(ownerPassword, userPassword, accessPermission)
        protectionPolicy.encryptionKeyLength = 128 // 128-bit or 256-bit encryption
        protectionPolicy.permissions = accessPermission

        // Apply the protection policy
        document.protect(protectionPolicy)

        // Save the locked PDF
        document.save(outputPath)
        document.close()

        println("PDF locked successfully: $outputPath")
    } catch (e: Exception) {
        e.printStackTrace()
        println("Failed to lock PDF: ${e.message}")
    }
}

fun splitPdf(
    context: Context,
    document: Document,
    selectedFiles: List<Int>
): Flow<PdfOperationState<List<Uri>>> = flow {
    // Emit the initial idle state
    emit(PdfOperationState.Idle)

    // Emit the loading state
    emit(PdfOperationState.Loading)

    val pdfUris = mutableListOf<Uri>()

    try {
        // Perform IO operation within Dispatchers.IO
        withContext(Dispatchers.IO) {
            val pdDocument = PDDocument.load(File(document.path)) // Load the document

            // Filter valid page indices to avoid unnecessary errors
            val validPages = selectedFiles.filter { it in 0 until pdDocument.numberOfPages }
            if (validPages.isEmpty()) {
                throw IllegalArgumentException("No valid pages selected")
            }

            // Process valid pages
            validPages.forEach { pageIndex ->
                val splitDoc = PDDocument().apply { addPage(pdDocument.getPage(pageIndex)) }
                val fileName = "${document.name.removeSuffix(".pdf")}_page-${pageIndex + 1}.pdf"
                pdfUris.add(savePdfToDownloads(context, splitDoc, document.name.removeSuffix(".pdf"), fileName))
                splitDoc.close()
            }

            pdDocument.close()
        }

        // Emit the success state with the list of URIs
        emit(PdfOperationState.Success(pdfUris))
    } catch (e: Exception) {
        e.printStackTrace()

        // Emit an error state in case of an exception
        emit(PdfOperationState.Error("An error occurred: ${e.message}"))
    }
}.catch { e ->
    // Emit an error state in case of an uncaught exception
    emit(PdfOperationState.Error("An error occurred: ${e.message}"))
}

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