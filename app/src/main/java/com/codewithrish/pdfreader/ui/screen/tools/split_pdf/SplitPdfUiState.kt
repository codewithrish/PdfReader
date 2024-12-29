package com.codewithrish.pdfreader.ui.screen.tools.split_pdf

import androidx.compose.ui.graphics.ImageBitmap
import com.codewithrish.pdfreader.core.model.home.Document

data class SplitPdfUiState(
    val isLoading: Boolean = false,
    val document: Document? = null,
    val splitPdfDocuments: List<Document> = emptyList(),
    val pages: List<ImageBitmap> = emptyList(),
    val selectedPages: List<Int> = emptyList(),
    val viewPage: ImageBitmap? = null,
    val errorMessage: Pair<Boolean, String> = Pair(false, ""),
)