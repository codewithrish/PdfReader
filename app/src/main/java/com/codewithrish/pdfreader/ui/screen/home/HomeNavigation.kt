package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.codewithrish.pdfreader.core.model.home.Document
import kotlinx.serialization.Serializable

@Serializable data object HomeGraph
@Serializable data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions = navOptions)

fun NavGraphBuilder.homeSection(
    onDocumentClick: (Document) -> Unit,
    onSettingsClick: () -> Unit,
) {
    navigation<HomeGraph>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val homeUiState by homeViewModel.state.collectAsStateWithLifecycle()
            val homeUiEvent = homeViewModel.onEvent

            Surface {
                HomeScreen(
                    state = homeUiState,
                    onEvent = homeUiEvent,
                    onDocumentClick = onDocumentClick,
                    onSettingsClick = onSettingsClick
                )
            }
        }
    }
}