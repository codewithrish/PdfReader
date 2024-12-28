package com.codewithrish.pdfreader.ui.screen.tools.split_pdf

import androidx.compose.ui.graphics.ImageBitmap
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.helper.PdfOperationStateEnum

data class SplitPdfUiState(
    val isLoading: Boolean = false,
    val document: Document? = null,
    val splitPdfUris: List<String> = emptyList(),
    val pages: List<ImageBitmap> = emptyList(),
    val selectedPages: List<Int> = emptyList(),
    val viewPage: ImageBitmap? = null,
    val errorMessage: Pair<Boolean, String> = Pair(false, ""),
    val pdfOperationStateEnum: PdfOperationStateEnum = PdfOperationStateEnum.IDLE
)