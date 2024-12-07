package com.codewithrish.pdfreader.ui.screen.home

sealed class HomeUiEvent {
    data object OnDocumentsLoad: HomeUiEvent()
    data class OpenDocument(val documentJson: String): HomeUiEvent()
}