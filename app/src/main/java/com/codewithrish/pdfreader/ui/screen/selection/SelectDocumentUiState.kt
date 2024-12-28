package com.codewithrish.pdfreader.ui.screen.selection

import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.screen.tools.ToolType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SelectDocumentUiState(
    val isLoading: Boolean = true,
    val toolType: ToolType = ToolType.DEFAULT,
    val documents: Flow<List<Document>> = emptyFlow(),
    val selectedDocuments: List<Document> = emptyList(),
    val errorMessage: Pair<String, String> = Pair("Select Document Screen Error", "Something went wrong")
)