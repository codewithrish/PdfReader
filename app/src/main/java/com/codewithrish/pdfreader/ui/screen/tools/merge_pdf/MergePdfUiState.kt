package com.codewithrish.pdfreader.ui.screen.tools.merge_pdf

import com.codewithrish.pdfreader.core.model.home.Document
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MergePdfUiState (
    val isLoading: Boolean = false,
    val selectedDocuments: Flow<List<Document>> = emptyFlow(),
    val mergedPdfDocument: Document? = null,
    val errorMessage: Pair<Boolean, String> = Pair(false, ""),
)