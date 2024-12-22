package com.codewithrish.pdfreader.ui.screen.selection

import com.codewithrish.pdfreader.core.model.home.Document

sealed class SelectDocumentUiEvent {
    // Load
    data object LoadDocuments: SelectDocumentUiEvent()
    // Operation
    data class OpenDocument(val image: Document): SelectDocumentUiEvent()
    data class DeleteDocument(val document: Document): SelectDocumentUiEvent()
    // Selection
    data class SelectDocument(val document: Document): SelectDocumentUiEvent()
    data class UnSelectDocument(val document: Document): SelectDocumentUiEvent()
}