package com.codewithrish.pdfreader.ui.screen.document_viewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent
import com.codewithrish.pdfreader.ui.components.EmptyScreenWithText
import com.codewithrish.pdfreader.ui.screen.document_viewer.viewer.pdf.PdfViewBottomBar
import com.codewithrish.pdfreader.ui.screen.document_viewer.viewer.pdf.PdfViewTopBar
import com.codewithrish.pdfreader.ui.screen.document_viewer.viewer.pdf.PdfViewerPdfium
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import com.codewithrish.pdfreader.ui.theme.materialColor

/**
 * [ViewDocumentViewModel]
 */

@OptIn(ExperimentalMaterial3Api::class)
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

    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (state.toggleBottomSheet) {
        ModalBottomSheet(
            sheetState = modalBottomSheetState,
            onDismissRequest = { onEvent(ViewDocumentUiEvent.ToggleBottomSheet) },
        ) {
            LazyColumn {
                item {
                    PdfOption(icon = CwrIcons.LockPdf, name = "Set Password")
                }
                item {
                    PdfOption(icon = CwrIcons.Compress, name = "Compress Pdf")
                }
                item {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), thickness = 1.dp, color = materialColor().outlineVariant)
                }
                item {
                    PdfOption(icon = CwrIcons.Print, name = "Print")
                }
                item {
                    PdfOption(icon = CwrIcons.Share, name = "Share")
                }
            }
        }
    }

    TrackScreenViewEvent(screenName = "Document View Screen")
}

@Composable
fun PdfOption(
    icon: ImageVector,
    name: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {}
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name
        )
        CwrText(
            text = name,
        )
    }
}