package com.codewithrish.pdfreader.ui.screen.tools.merge_pdf

import com.codewithrish.pdfreader.core.model.home.Document

data class MergePdfUiState (
    val isLoading: Boolean = false,
    val selectedDocuments: List<Document> = emptyList(),
    val mergedPdfDocument: Document? = null,
    val errorMessage: Pair<Boolean, String> = Pair(false, ""),
)