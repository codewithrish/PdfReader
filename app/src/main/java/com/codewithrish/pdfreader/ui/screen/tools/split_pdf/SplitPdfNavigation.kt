package com.codewithrish.pdfreader.ui.screen.tools.split_pdf

import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.navigation.serializer.genericNavType
import com.codewithrish.pdfreader.navigation.wrapper.animatedComposable
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable class SplitPdfRoute(val documentId: Long)

fun NavController.navigateToSplitPdf(documentId: Long) = navigate(route = SplitPdfRoute(documentId))

fun NavGraphBuilder.splitPdfScreen(
    goBack: () -> Unit,
) {
    animatedComposable<SplitPdfRoute>(
        typeMap = mapOf(typeOf<Document>() to genericNavType<Document>())
    ) {backStackEntry ->
        val documentId = backStackEntry.toRoute<SplitPdfRoute>().documentId
        val splitPdfViewModel: SplitPdfViewModel = hiltViewModel()
        val splitPdfUiState by splitPdfViewModel.state.collectAsStateWithLifecycle()
        val splitPdfUiEvent = splitPdfViewModel.onEvent

//        val context = LocalContext.current
//        val imageBitmaps: List<ImageBitmap> = PdfUtils.getPdfPages(context, Uri.parse(document.uri)).map { it.asImageBitmap() }

        LaunchedEffect(documentId) {
            splitPdfUiEvent(SplitPdfUiEvent.LoadDocument(documentId))
        }

        Surface {
            SplitPdfScreen(
                state = splitPdfUiState,
                onEvent = splitPdfUiEvent,
                goBack = goBack
            )
        }
    }
}