package com.codewithrish.pdfreader.ui.screen.tools

sealed class ToolsUiEvent {
    data class OnToolsClick(val toolType: ToolType): ToolsUiEvent()
}