package com.codewithrish.pdfreader.ui.screen.selection

import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.navigation.serializer.enumNavType
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import com.codewithrish.pdfreader.ui.screen.tools.ToolType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable class SelectDocumentRoute(val toolType: ToolType)

fun NavController.navigateToSelectDocument(
    toolType: ToolType,
) = navigate(route = SelectDocumentRoute(toolType = toolType))

fun NavGraphBuilder.selectDocumentScreen(
    goBack: () -> Unit,
    goToToolScreen: (ToolType, Document?, List<Long>?) -> Unit,
) {
    animatedComposable<SelectDocumentRoute>(
        typeMap = mapOf(typeOf<ToolType>() to enumNavType<ToolType>())
    ) {backStackEntry ->
        val toolType = backStackEntry.toRoute<SelectDocumentRoute>().toolType
        val selectDocumentViewModel: SelectDocumentViewModel = hiltViewModel()
        val selectDocumentUiState by selectDocumentViewModel.state.collectAsStateWithLifecycle()
        val selectDocumentUiEvent = selectDocumentViewModel.onEvent

        LaunchedEffect(toolType) {
            selectDocumentUiEvent(SelectDocumentUiEvent.SaveToolType(toolType))
        }

        Surface {
            SelectDocumentScreen(
                state = selectDocumentUiState,
                onEvent = selectDocumentUiEvent,
                goToToolScreen = goToToolScreen,
                goBack = goBack
            )
        }
    }
}