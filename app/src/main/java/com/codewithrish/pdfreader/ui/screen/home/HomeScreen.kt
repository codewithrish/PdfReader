package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.components.EmptyScreenWithText
import com.codewithrish.pdfreader.ui.components.LoadingScreen
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
            CwrContentBox(paddingValues = PaddingValues(top = paddingValues.calculateTopPadding(), bottom = 0.dp)) {
                if (state.isLoading) {
                    LoadingScreen()
                }
                if (!state.isLoading) {
                    state.documents.collectAsStateWithLifecycle(emptyList()).value.let { documents ->
                        if (documents.isNotEmpty()) {
                            documents.forEach { document ->
                                onEvent(HomeUiEvent.CheckFileExists(document.uri))
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
                                EmptyScreenWithText("No Documents Found")
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
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CwrText(
            text = stringResource(R.string.app_name),
            style = materialTextStyle().titleLarge,
            modifier = Modifier.weight(1f)
        )
        Row (
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = CwrIcons.Search,
                contentDescription = "",
                tint = materialColor().onSurface,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                imageVector = CwrIcons.Settings,
                contentDescription = "",
                tint = materialColor().onSurface,
                modifier = Modifier.clickable {
                    onSettingsClick()
                }
            )
        }
    }
}

