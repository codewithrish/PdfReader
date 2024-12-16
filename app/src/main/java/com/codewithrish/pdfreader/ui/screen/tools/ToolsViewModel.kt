package com.codewithrish.pdfreader.ui.screen.tools

import com.codewithrish.pdfreader.core.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ToolsViewModel @Inject constructor(

) : BaseViewModel<ToolsUiState, ToolsUiEvent>() {
    override fun initState(): ToolsUiState = ToolsUiState()

    override fun onEvent(event: ToolsUiEvent) {
        when (event) {
            is ToolsUiEvent.OnToolsClick -> {}
        }
    }
}