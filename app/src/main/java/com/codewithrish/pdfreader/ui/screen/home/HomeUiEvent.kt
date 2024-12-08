package com.codewithrish.pdfreader.ui.screen.home

import com.codewithrish.pdfreader.core.model.home.Document

sealed class HomeUiEvent {
    data object OnDocumentsLoad: HomeUiEvent()
    data class OpenDocument(val document: Document): HomeUiEvent()
}