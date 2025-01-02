package com.codewithrish.pdfreader.ui.screen.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codewithrish.pdfreader.core.designsystem.component.CwrButton
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent
import com.codewithrish.pdfreader.ui.components.LoadingScreen
import com.codewithrish.pdfreader.ui.screen.selection.components.SelectDocumentContent
import com.codewithrish.pdfreader.ui.screen.tools.ToolType
import com.codewithrish.pdfreader.ui.theme.Shape
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

/**
 * [SelectDocumentViewModel]
 */

@Composable
fun SelectDocumentScreen(
    state: SelectDocumentUiState,
    onEvent: (SelectDocumentUiEvent) -> Unit,
    goBack: () -> Unit,
    goToToolScreen: (ToolType, Document?, List<Long>?) -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> { onEvent(SelectDocumentUiEvent.LoadDocuments) }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold (
        topBar = {
            SelectDocumentTopBar(
                toolType = state.toolType,
                goBack = goBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
            )
        },
        content = { paddingValues ->
            CwrContentBox(
                paddingValues =   paddingValues//PaddingValues(top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding())
            ) {
                if (state.isLoading) {
                    LoadingScreen()
                }
                if (!state.isLoading) {
                    state.documents.collectAsStateWithLifecycle(emptyList()).value.let { documents ->
                        if (documents.isNotEmpty()) {
                            documents.forEach { document ->
                                onEvent(SelectDocumentUiEvent.CheckFileExists(document.uri))
                            }.also {
                                SelectDocumentContent(
                                    toolType = state.toolType,
                                    documents = documents,
                                    selectedItems = state.selectedDocuments,
                                    goToToolScreen = goToToolScreen,
                                    onEvent = onEvent,
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (state.toolType.multiSelection && !state.isLoading) {
                SelectDocumentBottomBar(
                    state = state,
                    goToToolScreen = goToToolScreen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()),
                )
            }
        }
    )

    TrackScreenViewEvent(screenName = "Select Document Screen")
}

@Composable
fun SelectDocumentTopBar(
    toolType: ToolType,
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
) {
    Row(
        modifier = modifier
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
        val title = when (toolType) {
            ToolType.SPLIT_PDF -> "Select Pdf To Split"
            ToolType.MERGE_PDF -> "Select Pdfs To Merge"
            ToolType.IMAGE_TO_PDF -> "Select Images To Convert"
            ToolType.DEFAULT -> "Select Documents To Convert"
        }
        CwrText(
            text = title,
            style = materialTextStyle().titleLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SelectDocumentBottomBar(
    state: SelectDocumentUiState,
    goToToolScreen: (ToolType, Document?, List<Long>?) -> Unit,
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
        CwrButton(
            text = "Proceed",
            enabled = state.selectedDocuments.isNotEmpty(),
            modifier = modifier
                .wrapContentHeight()
                .weight(1f),
            onClick = {
                goToToolScreen(state.toolType, null, state.selectedDocuments.map { it.id })
            }
        )
    }
}