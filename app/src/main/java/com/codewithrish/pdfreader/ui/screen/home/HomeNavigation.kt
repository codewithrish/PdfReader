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
import kotlinx.serialization.Serializable

@Serializable data object HomeRoute
@Serializable data object HomeBaseRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions = navOptions)

fun NavGraphBuilder.homeSection(
    onDocumentClick: (String) -> Unit,
) {
    navigation<HomeBaseRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val homeUiState by homeViewModel.state.collectAsStateWithLifecycle()
            val homeUiEvent = homeViewModel.onEvent

            Surface {
                HomeScreen(
                    state = homeUiState,
                    onEvent = { event ->
                        when (event) {
                            is HomeUiEvent.OpenDocument -> onDocumentClick(event.documentJson)
                            is HomeUiEvent.OnDocumentsLoad -> homeUiEvent(event)
                        }
                    }
                )
            }
        }
    }
}