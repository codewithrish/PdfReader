package com.codewithrish.pdfreader.ui.screen.selection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent
import com.codewithrish.pdfreader.ui.components.LoadingScreen
import com.codewithrish.pdfreader.ui.screen.selection.components.DocumentGrid
import java.io.File

/**
 * [SelectDocumentViewModel]
 */

@Composable
fun SelectDocumentScreen(
    state: SelectDocumentUiState,
    onEvent: (SelectDocumentUiEvent) -> Unit,
    goBack: () -> Unit,
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
        topBar = { SelectDocumentTopBar(goBack = goBack) },
        content = { paddingValues ->
            CwrContentBox(paddingValues = paddingValues) {
                if (state.isLoading) {
                    LoadingScreen()
                }
                if (!state.isLoading) {
                    state.documents.collectAsStateWithLifecycle(emptyList()).value.let { documents ->
                        if (documents.isNotEmpty()) {
                            documents.forEach { document ->
                                val file = File(document.path)
                                if (!file.exists()) {
                                    onEvent(SelectDocumentUiEvent.DeleteDocument(document))
                                }
                            }.also {
                                DocumentGrid(
                                    documents = documents,
                                    selectedItems = state.selectedDocuments,
                                    onEvent = onEvent,
                                    modifier = modifier.padding(paddingValues)
                                )
                            }
                        }
                    }
                }
            }
        }
    )

    TrackScreenViewEvent(screenName = "Select Document Screen")
}

@Composable
fun SelectDocumentTopBar(
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(56.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = CwrIcons.BackArrow,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.clickable { goBack() }
        )
        CwrText(
            text = "Select Documents",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}