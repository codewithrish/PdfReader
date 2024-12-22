package com.codewithrish.pdfreader.ui.screen.home

import com.codewithrish.pdfreader.core.model.home.Document
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeUiState (
    val isLoading: Boolean = true,
    val documents: Flow<List<Document>> = emptyFlow(),
    val errorMessage: Pair<String, String> = Pair("Home Screen Error", "Something went wrong")
)