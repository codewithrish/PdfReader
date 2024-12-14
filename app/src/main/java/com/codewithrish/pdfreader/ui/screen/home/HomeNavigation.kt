package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import com.codewithrish.pdfreader.ui.screen.document_viewer.documentScreen
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable data object HomeGraph
@Serializable data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions = navOptions)

fun NavGraphBuilder.homeSection(
    onDocumentClick: (Document) -> Unit,
) {
    navigation<HomeGraph>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val homeUiState by homeViewModel.state.collectAsStateWithLifecycle()
            val homeUiEvent = homeViewModel.onEvent

            Surface {
                HomeScreen(
                    state = homeUiState,
                    onEvent = { event ->
                        when (event) {
                            is HomeUiEvent.OnDocumentsLoadInDb -> homeUiEvent(event)
                            is HomeUiEvent.OnDocumentsLoad -> homeUiEvent(event)
                            is HomeUiEvent.OpenDocument -> onDocumentClick(event.document)
                            is HomeUiEvent.DeleteDocument -> {
                                homeUiEvent(event)
                            }
                        }
                    }
                )
            }
        }
    }
}