package com.codewithrish.pdfreader.ui.screen.bookmark

import com.codewithrish.pdfreader.core.model.home.Document
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class BookmarksUiState (
    val isLoading: Boolean = true,
    val documents: Flow<List<Document>> = emptyFlow(),
    val errorMessage: Pair<String, String> = Pair("Bookmarks Screen Error", "Something went wrong")
)