package com.codewithrish.pdfreader.ui.screen.home

import com.codewithrish.pdfreader.core.model.home.Document

sealed class HomeUiEvent {
    data object OnDocumentsLoadInDb: HomeUiEvent()
    data object OnDocumentsLoad: HomeUiEvent()
    data class ShareDocument(val document: Document): HomeUiEvent()
    data class PrintDocument(val document: Document): HomeUiEvent()
    data class DeleteDocument(val document: Document): HomeUiEvent()
    data class OpenDocument(val document: Document): HomeUiEvent()
    data class OnBookmarkClick(val id: Long, val isBookmarked: Boolean): HomeUiEvent()
    data class CheckFileExists(val uri: String): HomeUiEvent()
    data class ShowHideDocumentOptions(val showHide: Boolean, val documentId: Long? = null): HomeUiEvent()
}