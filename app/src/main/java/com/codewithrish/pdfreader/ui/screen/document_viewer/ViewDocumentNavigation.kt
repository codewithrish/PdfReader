package com.codewithrish.pdfreader.ui.screen.document_viewer

import android.net.Uri
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.toRoute
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.navigation.serializer.genericNavType
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import com.codewithrish.pdfreader.ui.helper.getPdfPagesIo
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable data class ViewDocumentRoute(val document: Document)

fun NavController.navigateToViewDocument(
    document: Document,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = ViewDocumentRoute(document = document), navOptions)

fun NavGraphBuilder.documentScreen(
    goBack: () -> Unit,
) {
    animatedComposable<ViewDocumentRoute>(
        typeMap = mapOf(typeOf<Document>() to genericNavType<Document>())
    ) { backStackEntry ->
        val document = backStackEntry.toRoute<ViewDocumentRoute>().document
        val viewDocumentViewModel: ViewDocumentViewModel = hiltViewModel()
        val viewDocumentUiState by viewDocumentViewModel.state.collectAsStateWithLifecycle()
        val viewDocumentUiEvent = viewDocumentViewModel.onEvent

        val context = LocalContext.current

        LaunchedEffect(document) {
            val imageBitmaps = getPdfPagesIo(context, Uri.parse(document.uri)).map { it.asImageBitmap() }
            viewDocumentUiEvent(ViewDocumentUiEvent.LoadDocument(document, imageBitmaps))
        }

        Surface {
            ViewDocumentScreen(
                state = viewDocumentUiState,
                onEvent = viewDocumentUiEvent,
                goBack = goBack
            )
        }
    }
}