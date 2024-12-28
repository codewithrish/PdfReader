package com.codewithrish.pdfreader.ui.screen.document_viewer

import androidx.compose.ui.graphics.ImageBitmap
import com.codewithrish.pdfreader.core.model.home.Document

sealed class ViewDocumentUiEvent {
    data class LoadDocument(val document: Document, val pages: List<ImageBitmap>): ViewDocumentUiEvent()
    data class OnPageChange(val pageNumber: Pair<Int, Int>): ViewDocumentUiEvent()
    data object ToggleTopBottomBars: ViewDocumentUiEvent()
    data object ToggleBottomSheet: ViewDocumentUiEvent()
}