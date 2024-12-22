package com.codewithrish.pdfreader.ui.screen.tools.split_pdf

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.components.DocumentDetails
import com.codewithrish.pdfreader.ui.components.LoadingScreen
import com.codewithrish.pdfreader.ui.components.PdfPagesGrid
import com.codewithrish.pdfreader.ui.components.EmptyScreenWithText
import com.codewithrish.pdfreader.ui.helper.PdfUtils
import com.codewithrish.pdfreader.ui.helper.SplitPdfResult
import com.codewithrish.pdfreader.ui.theme.titleLargeTextStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class SplitPdfState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}

@Composable
fun SplitPdfScreen(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val pickPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            println("Picked file URI: $uri")
            selectedFileUri = uri
        }
    }
    // State to store PDF pages
    var pages by remember { mutableStateOf<List<Bitmap>>(emptyList()) }
    // State to store Selected PDF indices
    val selectedItems = remember { mutableStateListOf<Int>() }

    // State to track split PDF URIs
    val splitPdfUris = remember { mutableStateListOf<Uri>() }
    // State to check split status
    var splittingPdfs by remember { mutableStateOf(SplitPdfState.IDLE) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = selectedFileUri) {
        selectedFileUri?.let {
            pages = PdfUtils.getPdfPages(context, it)
        }
    }

    var document by remember { mutableStateOf<Document?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        when (splittingPdfs) {
            SplitPdfState.IDLE -> {
                // UI to trigger file picker
                Button(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick = {
                        selectedFileUri = null

                        pages = emptyList()
                        selectedItems.clear()

                        splitPdfUris.clear()
                        splittingPdfs = SplitPdfState.IDLE

                        pickPdfLauncher.launch("application/pdf")
                    }
                ) {
                    CwrText("Pick a PDF File")
                }

                if (selectedFileUri != null) {
                    document = PdfUtils.getDocumentFromUri(context, selectedFileUri!!)
                    document?.let {
                        CwrCardView(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            DocumentDetails(document = it, noOfPages = PdfUtils.getPageCountFromUri(context, Uri.parse(it.uri)), modifier = modifier)
                        }
                    }
                } else {
                    EmptyScreenWithText(message = "Your File Details and Pages will be displayed here")
                }
                if (selectedItems.isNotEmpty() ) {
                    OutlinedButton (
                        onClick = {
                            splittingPdfs = SplitPdfState.LOADING
                            coroutineScope.launch {
                                val result = withContext(Dispatchers.IO) {
                                    PdfUtils.splitPdf(context, document!!, selectedItems.toList().sorted())
                                }
                                when (result) {
                                    is SplitPdfResult.Success -> {
                                        selectedItems.clear()
                                        splitPdfUris.clear()
                                        splitPdfUris.addAll(result.uris)
                                        splittingPdfs = SplitPdfState.SUCCESS
                                    }
                                    is SplitPdfResult.Error -> {
                                        splittingPdfs = SplitPdfState.ERROR
                                    }
                                }
                            }
                        },
                        shape = MaterialTheme.shapes.small,
                        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.img_scissor),
                            contentDescription = null,
                            modifier
                                .padding(horizontal = 16.dp)
                                .size(24.dp)
                        )
                        CwrText(text = "Split Selected Pages")
                    }
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CwrText(
                        text = "PDF Pages (Selected ${selectedItems.size})",
                        modifier = Modifier.padding(16.dp)
                    )
                    Image(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        colorFilter = ColorFilter.tint(if (selectedItems.size == pages.size && selectedItems.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .padding(16.dp).
                            clickable {
                                if (selectedItems.size != pages.size) {
                                    selectedItems.clear()
                                    selectedItems.addAll(pages.indices.toList())
                                } else {
                                    selectedItems.clear()
                                }
                            }
                    )
                }
                if (pages.isNotEmpty()) {
                    PdfPagesGrid(
                        pages = pages,
                        selectedItems = selectedItems
                    ) { index ->
                        if (selectedItems.contains(index)) {
                            selectedItems.remove(index)
                        } else {
                            selectedItems.add(index)
                        }
                    }
                }
            }
            SplitPdfState.LOADING -> {
                LoadingScreen()
            }
            SplitPdfState.SUCCESS -> {
                CwrText(
                    text = "Extracted Pdfs",
                    style = titleLargeTextStyle(),
                    modifier = modifier.fillMaxWidth().padding(16.dp)
                )
                OutlinedButton(
                    onClick = {
                        openFileLocation(context, splitPdfUris.first())
                    },
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                    modifier = modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        modifier
                            .padding(horizontal = 16.dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    CwrText(text = "Visit Files Location")
                }
                LazyColumn(
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(splitPdfUris) { splitPdfUri ->
                        CwrCardView {
                            DocumentDetails(PdfUtils.getDocumentFromUri(context, splitPdfUri)!!, noOfPages = PdfUtils.getPageCountFromUri(context, splitPdfUri))
                        }
                    }
                }
            }
            SplitPdfState.ERROR -> {
                EmptyScreenWithText("Error splitting PDF", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

fun openFileLocation(context: Context, uri: Uri) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "*/*") // Use MIME type based on the file type (e.g., "application/pdf" for PDFs)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION // Grant temporary read permission
        }
        context.startActivity(intent)
    } catch (e: Exception) {
//        Toast.makeText(context, "Cannot open file location: ${e.message}", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
}
