package com.codewithrish.pdfreader.ui.screen.document_viewer.viewer.pdf

import android.net.Uri
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.helper.unlockPdf
import com.codewithrish.pdfreader.ui.screen.document_viewer.ViewDocumentUiEvent
import com.codewithrish.pdfreader.ui.screen.document_viewer.ViewDocumentUiState
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle

@Composable
fun PdfViewerPdfium(
    document: Document,
    state: ViewDocumentUiState,
    onEvent: (ViewDocumentUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Reference to the PDFView for programmatic control
    val pdfViewRef = remember { mutableStateOf<PDFView?>(null) }
    val isDark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
//        if (state.document?.isLocked == true)
//            unlockPdf(document.path, document.path, "Pdf@1234")
        AndroidView(
            factory = { ctx ->
                PDFView(ctx, null).apply {
                    pdfViewRef.value = this
                    setOnClickListener {
                        onEvent(ViewDocumentUiEvent.ToggleTopBottomBars)
                    }
                    fromUri(Uri.parse(document.uri))
                        .enableSwipe(true) // Allows swiping between pages
                        .swipeHorizontal(false) // Horizontal swipe disabled
                        .enableDoubletap(true) // Allows double-tap zooming
                        .defaultPage(state.pageNumber.first) // Start at the first page
                        .nightMode(isDark)
                        .pageSnap(false)
                        .scrollHandle(DefaultScrollHandle(ctx))
                        .onPageChange { page, pageCount ->
                            onEvent(ViewDocumentUiEvent.OnPageChange(Pair(page, pageCount)))
                        }
                        .load()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
    }
    val pdfView = pdfViewRef.value
    pdfView?.jumpTo(state.pageNumber.first)
}