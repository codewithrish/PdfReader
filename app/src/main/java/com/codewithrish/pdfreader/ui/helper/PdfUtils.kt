package com.codewithrish.pdfreader.ui.helper

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File

object PdfUtils {

    fun splitPdf(context: Context, inputPdfPath: String): List<Uri> {
        val pdfUris = mutableListOf<Uri>()
        try {
            val document = PDDocument.load(File(inputPdfPath))
            for (i in 0 until document.numberOfPages) {
                val splitDoc = PDDocument().apply { addPage(document.getPage(i)) }
                pdfUris.add(savePdfToDownloads(context, splitDoc, "page-${i + 1}.pdf"))
                splitDoc.close()
            }
            document.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pdfUris
    }

    private fun savePdfToDownloads(context: Context, document: PDDocument, fileName: String): Uri {
        val resolver = context.contentResolver
        val pdfUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/SplitPdfs")
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
}
