package com.codewithrish.pdfreader.ui.screen.tools.merge_pdf

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codewithrish.pdfreader.core.designsystem.component.CwrButton
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.ui.screen.home.DocumentDetails
import com.codewithrish.pdfreader.ui.theme.Shape
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

@Composable
fun MergePdfScreen(
    state: MergePdfUiState,
    onEvent: (MergePdfUiEvent) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold (
        topBar = {
            MergePdfTopBar(
                state = state,
                goBack = goBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
            )
        },
        content = { paddingValues ->
            CwrContentBox(
                modifier = modifier.fillMaxSize(),
                paddingValues = paddingValues,
                contentAlignment = Alignment.TopCenter
            ) {
                state.mergedPdfDocument?.let {
                    DocumentDetails(
                        document = state.mergedPdfDocument,
                    )
                } ?: run {
                    state.selectedDocuments.collectAsStateWithLifecycle(emptyList()).value.let { selectedDocuments ->
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            itemsIndexed(
                                items = selectedDocuments,
                                key = { index, document -> document.id }
                            ) { index, document ->
                                DocumentDetails(
                                    document = document,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            MergePdfBottomBar(
                state = state,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()),
            )
        }
    )
}

@Composable
fun MergePdfTopBar(
    state: MergePdfUiState,
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
            text = if (state.mergedPdfDocument != null) "Your Merged Pdf" else "Selected Pdfs to Merge",
            style = materialTextStyle().titleLarge,
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
        )
    }
}

@Composable
fun MergePdfBottomBar(
    state: MergePdfUiState,
    onEvent: (MergePdfUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = Modifier
            .shadow(elevation = 8.dp, shape = Shape.noCornerShape)
            .background(color = materialColor().surface)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 12.dp, start = 16.dp, end = 16.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val selectedDocuments = state.selectedDocuments.collectAsStateWithLifecycle(emptyList()).value
        CwrButton(
            text = "Merge (${state.selectedDocuments.collectAsStateWithLifecycle(emptyList()).value.size}) Pdf Files",
            enabled = state.selectedDocuments.collectAsStateWithLifecycle(emptyList()).value.isNotEmpty() && !state.isLoading,
            modifier = modifier
                .wrapContentHeight()
                .weight(1f),
            onClick = {
                onEvent(MergePdfUiEvent.MergePdf(selectedDocuments, "Merged File.pdf"))
            }
        )
        AnimatedVisibility(
            visible = state.isLoading,
            enter = scaleIn(animationSpec = tween(durationMillis = 300)),
            exit = scaleOut(animationSpec = tween(durationMillis = 300))
        ) {
            CircularProgressIndicator(modifier = Modifier.size(32.dp))
        }
    }
}