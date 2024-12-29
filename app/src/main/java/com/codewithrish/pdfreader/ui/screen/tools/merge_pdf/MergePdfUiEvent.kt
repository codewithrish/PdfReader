package com.codewithrish.pdfreader.ui.screen.tools.merge_pdf

import com.codewithrish.pdfreader.core.model.home.Document

sealed class MergePdfUiEvent {
    data class LoadDocuments(val selectedDocumentIds: List<Long>): MergePdfUiEvent()
    data class MergePdf(val selectedDocuments: List<Document>, val outputFileName: String): MergePdfUiEvent()
}