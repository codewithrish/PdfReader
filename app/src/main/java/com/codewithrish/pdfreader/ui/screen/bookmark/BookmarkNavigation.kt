package com.codewithrish.pdfreader.ui.screen.bookmark

import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.codewithrish.pdfreader.core.model.home.Document
import kotlinx.serialization.Serializable

@Serializable object BookmarksRoute

fun NavController.navigateToBookmarks(navOptions: NavOptions) =
    navigate(route = BookmarksRoute, navOptions)

fun NavGraphBuilder.bookmarksScreen(
    onDocumentClick: (Document) -> Unit,
    onSettingsClick: () -> Unit,
) {
    composable<BookmarksRoute>() {
        val bookmarksViewModel: BookmarksViewModel = hiltViewModel()
        val bookmarksUiState by bookmarksViewModel.state.collectAsStateWithLifecycle()
        val bookmarksUiEvent = bookmarksViewModel.onEvent

        Surface {
            BookmarksScreen(
                state = bookmarksUiState,
                onEvent = bookmarksUiEvent,
                onDocumentClick = onDocumentClick,
                onSettingsClick = onSettingsClick
            )
        }
    }
}