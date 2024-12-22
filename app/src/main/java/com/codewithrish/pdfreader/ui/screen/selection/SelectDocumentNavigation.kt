package com.codewithrish.pdfreader.ui.screen.selection

import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import kotlinx.serialization.Serializable

@Serializable object SelectDocumentRoute

fun NavController.navigateToSelectDocument() = navigate(route = SelectDocumentRoute)

fun NavGraphBuilder.selectDocumentScreen(
    goBack: () -> Unit,
) {
    animatedComposable<SelectDocumentRoute> {
        val selectDocumentViewModel: SelectDocumentViewModel = hiltViewModel()
        val selectDocumentUiState by selectDocumentViewModel.state.collectAsStateWithLifecycle()
        val selectDocumentUiEvent = selectDocumentViewModel.onEvent

        Surface {
            SelectDocumentScreen(
                state = selectDocumentUiState,
                onEvent = selectDocumentUiEvent,
                goBack = goBack
            )
        }
    }
}