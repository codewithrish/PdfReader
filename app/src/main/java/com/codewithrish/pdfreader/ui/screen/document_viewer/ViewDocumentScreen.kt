package com.codewithrish.pdfreader.ui.screen.document_viewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent
import com.codewithrish.pdfreader.ui.components.EmptyScreenWithText
import com.codewithrish.pdfreader.ui.screen.document_viewer.viewer.pdf.PdfViewBottomBar
import com.codewithrish.pdfreader.ui.screen.document_viewer.viewer.pdf.PdfViewTopBar
import com.codewithrish.pdfreader.ui.screen.document_viewer.viewer.pdf.PdfViewerPdfium
import com.codewithrish.pdfreader.ui.screen.home.DocumentType

/**
 * [ViewDocumentViewModel]
 */

@Composable
fun ViewDocumentScreen(
    state: ViewDocumentUiState,
    onEvent: (ViewDocumentUiEvent) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold (
        topBar = {
            AnimatedVisibility(
                visible = state.toggleTopBottomBars,
                enter = slideInVertically(
                    initialOffsetY = { -it } // Starts above the screen
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -it } // Moves back above the screen
                )
            ) {
                PdfViewTopBar(
                    state = state,
                    onEvent = onEvent,
                    goBack = goBack,
                    modifier = modifier
                )
            }
        },
        content = { paddingValues ->
            CwrContentBox(
                paddingValues = paddingValues,
            ) {
                state.document?.let { document ->
                    when (document.mimeType) {
                        DocumentType.PDF.name -> PdfViewerPdfium(
                            document = state.document,
                            state = state,
                            onEvent = onEvent,
                            modifier = modifier
                        )
                        else -> EmptyScreenWithText("Unsupported file type")
                    }
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = state.toggleTopBottomBars,
                enter = slideInVertically(
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }
                )
            ) {
                PdfViewBottomBar(
                    state = state,
                    onEvent = onEvent,
                    modifier = modifier
                )
            }
        }
    )
    TrackScreenViewEvent(screenName = "Document View Screen")
}
