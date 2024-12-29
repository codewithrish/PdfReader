package com.codewithrish.pdfreader.ui.screen.tools.merge_pdf

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import kotlinx.serialization.Serializable

@Serializable data class MergePdfRoute(val selectedDocumentIds: List<Long>)

fun NavController.navigateToMergePdf(selectedDocumentIds: List<Long>) = navigate(route = MergePdfRoute(selectedDocumentIds))

fun NavGraphBuilder.mergePdfScreen(
    goBack: () -> Unit,
) {
    animatedComposable<MergePdfRoute>(
    ) { backStackEntry ->
        val selectedDocumentIds = backStackEntry.toRoute<MergePdfRoute>().selectedDocumentIds
        val mergePdfViewModel: MergePdfViewModel = hiltViewModel()
        val mergePdfUiState by mergePdfViewModel.state.collectAsStateWithLifecycle()
        val mergePdfUiEvent = mergePdfViewModel.onEvent

        LaunchedEffect(selectedDocumentIds) {
            mergePdfUiEvent(MergePdfUiEvent.LoadDocuments(selectedDocumentIds))
        }

        MergePdfScreen(
            state = mergePdfUiState,
            onEvent = mergePdfUiEvent,
            goBack = goBack
        )
    }
}