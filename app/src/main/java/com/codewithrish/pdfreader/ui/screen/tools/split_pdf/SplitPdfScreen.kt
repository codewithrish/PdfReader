package com.codewithrish.pdfreader.ui.screen.tools.split_pdf

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrButton
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviewDialog
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.component.CwrZoomableImage
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.components.CustomRadioButton
import com.codewithrish.pdfreader.ui.helper.PdfOperationState
import com.codewithrish.pdfreader.ui.helper.PdfOperationStateEnum
import com.codewithrish.pdfreader.ui.helper.PdfUtils
import com.codewithrish.pdfreader.ui.helper.getPdfPagesIo
import com.codewithrish.pdfreader.ui.helper.splitPdf
import com.codewithrish.pdfreader.ui.screen.home.DocumentDetails
import com.codewithrish.pdfreader.ui.theme.PdfReaderTheme
import com.codewithrish.pdfreader.ui.theme.Shape
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialShape
import com.codewithrish.pdfreader.ui.theme.materialTextStyle
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * [SplitPdfViewModel]
 */

@Composable
fun SplitPdfScreen(
    modifier: Modifier = Modifier,
    state: SplitPdfUiState,
    onEvent: (SplitPdfUiEvent) -> Unit,
    goBack: () -> Unit,
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val pages = remember { mutableStateListOf<ImageBitmap>() }

    LaunchedEffect(state.document) {
        // Check if the document is not null before proceeding
        state.document?.let {
            // Perform the operation to get the pages asynchronously
            coroutineScope.launch {
                val pageBitmaps = getPdfPagesIo(context, Uri.parse(state.document.uri)).map { it.asImageBitmap() }

                // Clear the existing pages and add the new pages
                pages.clear()
                pages.addAll(pageBitmaps)

                // Trigger the event with the updated pages list
                onEvent(SplitPdfUiEvent.ShowDocumentPages(pages))
            }
        }
    }

    Scaffold (
        topBar = {
            state.document?.let { document ->
                SplitPdfTopBar(
                    document = document,
                    state = state,
                    onEvent = onEvent,
                    goBack = goBack
                )
            }
        },
        content = { paddingValues ->
            Timber.tag("Split").d(state.pdfOperationStateEnum.name)
            if (state.pdfOperationStateEnum == PdfOperationStateEnum.SUCCESS) {
                CwrContentBox(
                    modifier = modifier.padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    ).fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.splitPdfUris) { splitPdfUri ->
                                DocumentDetails(PdfUtils.getDocumentFromUri(context, Uri.parse(splitPdfUri))!!, noOfPages = PdfUtils.getPageCountFromUri(context, Uri.parse(splitPdfUri)))
                            }
                        }
                    }
                }
            } else {
                CwrContentBox(
                    modifier = modifier.padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
                ) {
                    Column {
                        state.document?.let { document ->
                            DocumentDetails(
                                document = document,
                                noOfPages = PdfUtils.getDocumentFromUri(
                                    LocalContext.current,
                                    Uri.parse(document.uri)
                                )?.numberOfPages ?: 0,
                                optionTwoIcon = CwrIcons.Eye,
                                modifier = Modifier.padding(16.dp),
                            )
                            SplitPdfContent(
                                state = state,
                                onEvent = onEvent,
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (state.pdfOperationStateEnum != PdfOperationStateEnum.SUCCESS) {
                SplitPdfBottomBar(
                    state = state,
                    onEvent = onEvent
                )
            }
        }
    )

    AnimatedVisibility(
        visible = state.viewPage != null,
        enter = scaleIn(
            animationSpec = tween(durationMillis = 300), // Duration of the animation
            initialScale = 0f // Start from zero size
        ),
        exit = scaleOut(
            animationSpec = tween(durationMillis = 300), // Duration of the animation
            targetScale = 0f // End with zero size
        )
    ) {
        CwrPreviewDialog(
            onDismiss = { onEvent(SplitPdfUiEvent.ClosePage) }
        ) {
            state.viewPage?.let { bitmap ->
                CwrZoomableImage(
                    imageBitmap = bitmap,
                    contentDescription = "Full screen image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(Shape.large)
//                        .background(Color.White)
                )
            }
        }
    }
}

@Composable
fun SplitPdfTopBar(
    document: Document,
    state: SplitPdfUiState,
    onEvent: (SplitPdfUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = CwrIcons.BackArrow,
            contentDescription = "",
            tint = materialColor().onBackground,
            modifier = Modifier.clickable { goBack() }
        )
        CwrText(
            text = if (state.pdfOperationStateEnum == PdfOperationStateEnum.SUCCESS) "Extracted Pdfs" else "Split Pdf",
            style = materialTextStyle().titleLarge,
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
        )
        if (state.pdfOperationStateEnum != PdfOperationStateEnum.SUCCESS) {
            Row( // Group RadioButton and text together
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CustomRadioButton(
                    selected = state.selectedPages.size == state.pages.size,
                    onClick = { onEvent(SplitPdfUiEvent.SelectAllPages(state.pages, state.selectedPages)) },
                    color = if (state.pages.size == state.selectedPages.size) materialColor().primary else materialColor().onBackground,
                    unselectedColor = if (state.pages.size == state.selectedPages.size) materialColor().primary else materialColor().onBackground
                )
                CwrText(
                    text = "Select All",
                    style = materialTextStyle().titleMedium,
                    modifier = Modifier
                        .clickable (
                            onClick = { onEvent(SplitPdfUiEvent.SelectAllPages(state.pages, state.selectedPages)) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )
                )
            }
        }
    }
}

@Composable
fun SplitPdfBottomBar(
    state: SplitPdfUiState,
    onEvent: (SplitPdfUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Row (
        modifier = modifier
            .shadow(elevation = 8.dp, shape = Shape.noCornerShape)
            .background(color = materialColor().surface)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 12.dp, start = 16.dp, end = 16.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CwrButton(
            text = "Split Pdf Files (${state.selectedPages.size})",
            enabled = state.selectedPages.isNotEmpty() && state.pdfOperationStateEnum != PdfOperationStateEnum.LOADING,
            modifier = modifier
                .wrapContentHeight()
                .weight(1f),
            onClick = {
                val result = splitPdf(
                    context = context,
                    document = state.document!!,
                    selectedFiles = state.selectedPages
                )
                coroutineScope.launch {
                    result.collect { it1 ->
                        when (it1) {
                            is PdfOperationState.Idle -> { onEvent(SplitPdfUiEvent.PdfOperationState(PdfOperationStateEnum.IDLE)) }
                            is PdfOperationState.Loading -> { onEvent(SplitPdfUiEvent.PdfOperationState(PdfOperationStateEnum.LOADING)) }
                            is PdfOperationState.Error -> { onEvent(SplitPdfUiEvent.PdfOperationState(PdfOperationStateEnum.ERROR)) }
                            is PdfOperationState.Success -> { onEvent(SplitPdfUiEvent.PdfOperationState(PdfOperationStateEnum.SUCCESS, it1.data.map { it2 -> it2.toString() })) }
                        }
                    }
                }
            }
        )
        AnimatedVisibility(
            visible = state.pdfOperationStateEnum == PdfOperationStateEnum.LOADING,
            enter = scaleIn(animationSpec = tween(durationMillis = 300)),
            exit = scaleOut(animationSpec = tween(durationMillis = 300))
        ) {
            CircularProgressIndicator(modifier = Modifier.size(32.dp))
        }
    }
}


@CwrPreviews
@Composable
private fun SplitPdfBottomBarPreview() {
    PdfReaderTheme {
        SplitPdfBottomBar(
            state = SplitPdfUiState(),
            onEvent = {}
        )
    }
}
