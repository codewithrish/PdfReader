package com.codewithrish.pdfreader.ui.screen.tools

data class ToolsUiState (
    val isLoading: Boolean = false,
    val errorMessage: Pair<String, String> = Pair("Tools Screen Error", "Something went wrong")
)