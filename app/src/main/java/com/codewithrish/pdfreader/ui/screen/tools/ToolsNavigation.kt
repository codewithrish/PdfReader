package com.codewithrish.pdfreader.ui.screen.tools

import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.codewithrish.pdfreader.navigation.serializer.enumNavType
import com.codewithrish.pdfreader.navigation.serializer.genericNavType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable data object ToolsGraph
@Serializable data object ToolsRoute

fun NavController.navigateToTools(navOptions: NavOptions) =
    navigate(route = ToolsRoute, navOptions)

fun NavGraphBuilder.toolsSection(
    onToolClick: (ToolType) -> Unit,
) {
    navigation<ToolsGraph>(startDestination = ToolsRoute) {
        composable<ToolsRoute>() {
            val toolsViewModel: ToolsViewModel = hiltViewModel()
            val toolsUiState by toolsViewModel.state.collectAsStateWithLifecycle()
            val toolsUiEvent = toolsViewModel.onEvent

            Surface {
                ToolsScreen(
                    state = toolsUiState,
                    onEvent = { event ->
                        when (event) {
                            is ToolsUiEvent.OnToolsClick -> toolsUiEvent(event)
                        }
                    },
                    onToolClick = onToolClick
                )
            }
        }
    }
}