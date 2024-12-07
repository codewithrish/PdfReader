package com.codewithrish.pdfreader.ui.screen.tools

import androidx.compose.material3.Surface
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable data object ToolsRoute

fun NavController.navigateToTools(navOptions: NavOptions) =
    navigate(route = ToolsRoute, navOptions)

fun NavGraphBuilder.toolsScreen(

) {
    composable<ToolsRoute>() {
        Surface {
            ToolsScreen()
        }
    }
}