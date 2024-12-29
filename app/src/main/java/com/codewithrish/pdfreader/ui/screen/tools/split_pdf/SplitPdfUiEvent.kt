package com.codewithrish.pdfreader.ui.screen.tools.split_pdf

import androidx.compose.ui.graphics.ImageBitmap
import com.codewithrish.pdfreader.core.model.home.Document

sealed class SplitPdfUiEvent {
    data class LoadDocument(val documentId: Long): SplitPdfUiEvent()
    data class ShowDocumentPages(val pages: List<ImageBitmap>): SplitPdfUiEvent()
    data class TogglePageSelection(val pageNumber: Int): SplitPdfUiEvent()
    data class ViewPage(val page: ImageBitmap): SplitPdfUiEvent()
    data object ClosePage: SplitPdfUiEvent()
    data class SelectAllPages(val pages: List<ImageBitmap>, val selectedPages: List<Int>): SplitPdfUiEvent()
    data class SplitPdf(val document: Document, val selectedPages: List<Int>): SplitPdfUiEvent()
}