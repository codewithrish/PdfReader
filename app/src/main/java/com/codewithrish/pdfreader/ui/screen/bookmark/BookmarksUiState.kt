package com.codewithrish.pdfreader.ui.screen.bookmark

import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class BookmarksUiState (
    val isLoading: Boolean = false,
    val documents: Flow<List<DocumentEntity>> = emptyFlow(),
    val errorMessage: Pair<String, String> = Pair("Bookmarks Screen Error", "Something went wrong")
)