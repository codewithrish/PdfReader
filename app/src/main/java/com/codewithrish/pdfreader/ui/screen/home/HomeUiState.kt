package com.codewithrish.pdfreader.ui.screen.home

import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeUiState (
    val isLoading: Boolean = false,
    val documents: Flow<List<DocumentEntity>> = emptyFlow(),
    val errorMessage: Pair<String, String> = Pair("Home Screen Error", "Something went wrong")
)