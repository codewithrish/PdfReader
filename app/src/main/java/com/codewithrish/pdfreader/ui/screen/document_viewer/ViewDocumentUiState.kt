package com.codewithrish.pdfreader.ui.screen.document_viewer

import androidx.compose.ui.graphics.ImageBitmap
import com.codewithrish.pdfreader.core.model.home.Document

data class ViewDocumentUiState (
    val document: Document? = null,
    val pages: List<ImageBitmap> = emptyList(),
    val pageNumber: Pair<Int, Int> = Pair(0, 0),
    var toggleTopBottomBars: Boolean = true,
    val errorMessage: Pair<String, String> = Pair("View Document Screen Error", "Something went wrong")
)