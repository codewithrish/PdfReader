package com.codewithrish.pdfreader.ui.screen.tools

import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent
import com.codewithrish.pdfreader.ui.helper.PdfUtils

@Composable
fun ToolsScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

    var inputPdfPath by remember { mutableStateOf("$downloadsFolder/MultiPage.pdf") }
    var outputDirPath by remember { mutableStateOf("$downloadsFolder/SplitPdfs") }
    var outputPdfPath by remember { mutableStateOf("$downloadsFolder/Merged.pdf") }
    var pdfUris by remember { mutableStateOf(listOf<Uri>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = inputPdfPath,
            onValueChange = { inputPdfPath = it },
            label = { Text("Input PDF Path") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val splitFiles = PdfUtils.splitPdf(context, inputPdfPath)
                pdfUris = splitFiles // Save the Uris instead of File paths
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Split PDF")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = outputPdfPath,
            onValueChange = { outputPdfPath = it },
            label = { Text("Output Merged PDF Path") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                PdfUtils.mergePdfs(context, pdfUris, outputPdfPath)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Merge PDFs")
        }
    }
    TrackScreenViewEvent(screenName = "Tools Screen")
}

@CwrPreviews
@Composable
fun ToolsScreenPreview() {
    ToolsScreen()
}

