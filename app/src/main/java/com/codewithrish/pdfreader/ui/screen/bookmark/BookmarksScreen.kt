package com.codewithrish.pdfreader.ui.screen.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent
import com.codewithrish.pdfreader.ui.components.EmptyScreenWithText
import com.codewithrish.pdfreader.ui.components.LoadingScreen
import com.codewithrish.pdfreader.ui.screen.home.DocumentList
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

/**
 * [BookmarksViewModel]
 */

@Composable
fun BookmarksScreen(
    state: BookmarksUiState,
    onEvent: (BookmarksUiEvent) -> Unit,
    onDocumentClick: (Document) -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // Observe lifecycle events
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    onEvent(BookmarksUiEvent.LoadBookMarks)
                }
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var showEmptyScreen by remember { mutableStateOf(false) }

    Scaffold (
        topBar = { BookMarksTopBar(modifier = Modifier.fillMaxWidth()) },
        content = { paddingValues ->
            CwrContentBox(paddingValues = PaddingValues(top = paddingValues.calculateTopPadding(), bottom = 0.dp)) {
                if (state.isLoading) {
                    LoadingScreen()
                }
                if (!state.isLoading) {
                    state.documents.collectAsStateWithLifecycle(emptyList()).value.let { documents ->
                        if (documents.isNotEmpty()) {
                            documents.forEach { document ->
                                val file = File(document.path)
                                if (!file.exists()) {
                                    onEvent(BookmarksUiEvent.DeleteDocument(document))
                                }
                            }.also {
                                DocumentList(
                                    documents = documents
                                        .filter { it.mimeType == DocumentType.PDF.name },
                                    onDocumentClick = onDocumentClick,
                                    onBookmarkClick = { id, isBookmarked ->
                                        onEvent(BookmarksUiEvent.OnBookmarkClick(id, isBookmarked))
                                    },
                                    modifier = modifier.fillMaxSize()
                                )
                            }
                        } else {
                            lifecycleOwner.lifecycleScope.launch {
                                delay(300)
                                showEmptyScreen = true
                            }
                            if (showEmptyScreen) {
                                EmptyScreenWithText("Boo! Your bookmarks vanished.\nAdd some to bring them back!")
                            }
                        }
                    }
                }
            }
        }
    )
    TrackScreenViewEvent(screenName = "Bookmarks Screen")
}

@Composable
fun BookMarksTopBar(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(56.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CwrText(
            text = "Bookmarks",
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
            )
            Icon(
                imageVector = CwrIcons.Settings,
                contentDescription = "",
                tint = materialColor().onSurface,
            )
        }
    }
}

//@Composable
//fun BookMarksTopBar(modifier: Modifier = Modifier) {
//    Row(
//        modifier = modifier
//            .height(56.dp).padding(horizontal = 16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Icon(
//            imageVector = CwrIcons.Search,
//            contentDescription = "",
//            tint = materialColor().onSurface,
//        )
//        CwrText(
//            text = "Bookmarks",
//            style = materialTextStyle().titleLarge.copy(fontFamily = poetsenOneFontFamily)
//        )
//        Icon(
//            imageVector = CwrIcons.Settings,
//            contentDescription = "",
//            tint = materialColor().onSurface,
//        )
//    }
//}