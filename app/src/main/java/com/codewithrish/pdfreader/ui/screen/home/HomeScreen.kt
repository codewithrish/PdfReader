package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.components.EmptyScreenWithText
import com.codewithrish.pdfreader.ui.components.LoadingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

/**
 * [HomeViewModel]
 */

@Composable
fun HomeScreen(
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onDocumentClick: (Document) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    onEvent(HomeUiEvent.OnDocumentsLoadInDb)
                    onEvent(HomeUiEvent.OnDocumentsLoad)
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    var showEmptyScreen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            HomeTopBar(
                modifier = modifier.fillMaxWidth(),
                onSettingsClick = onSettingsClick
            )
        },
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
                                    onEvent(HomeUiEvent.DeleteDocument(document))
                                }
                            }.also {
                                DocumentList(
                                    documents = documents
                                        .filter { it.mimeType == DocumentType.PDF.name },
                                    onDocumentClick = onDocumentClick,
                                    onBookmarkClick = { id, isBookmarked ->
                                        onEvent(HomeUiEvent.OnBookmarkClick(id, isBookmarked))
                                    },
                                    modifier = Modifier
                                )
                            }
                        } else {
                            lifecycleOwner.lifecycleScope.launch {
                                delay(300)
                                showEmptyScreen = true
                            }
                            if (showEmptyScreen) {
                                EmptyScreenWithText("No Bookmarks Found")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(56.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = Icons.Default.Search,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        )
        CwrText(
            text = "Open Pdf",
            style = MaterialTheme.typography.titleLarge
        )
        Image(
            imageVector = Icons.Default.Settings,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.clickable {
                onSettingsClick()
            }
        )
    }
}

