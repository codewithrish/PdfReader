package com.codewithrish.pdfreader.ui.screen.bookmark

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.toDocument
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent
import com.codewithrish.pdfreader.ui.components.LoadingScreen
import com.codewithrish.pdfreader.ui.screen.home.DocumentList
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import timber.log.Timber
import java.io.File

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

    Scaffold (
        topBar = {
            BookMarksTopBar(modifier = Modifier.fillMaxWidth())
        },
        content = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                state.documents.collectAsStateWithLifecycle(emptyList()).value.let { documents ->
                    if (documents.isNotEmpty()) {
                        documents.forEach { document ->
                            val file = File(document.path)
                            Timber.tag("HomeScreen")
                                .d("File exists: ${document.path} ${file.exists()}")
                            if (!file.exists()) {
                                onEvent(BookmarksUiEvent.DeleteDocument(document.toDocument()))
                            }
                        }.also {
                            DocumentList(
                                documents = documents.map { it.toDocument() }
                                    .filter { it.mimeType == DocumentType.PDF.name },
                                onDocumentClick = onDocumentClick,
                                onBookmarkClick = { id, isBookmarked ->
                                    onEvent(BookmarksUiEvent.OnBookmarkClick(id, isBookmarked))
                                },
                                modifier = Modifier
                            )
                        }
                    } else {
//                        LoadingScreen()
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Bookmarks Found",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 32.sp,
                            )
                        }
                    }
                }
            }
        }
    )
    TrackScreenViewEvent(screenName = "Bookmarks Screen")
}

@Composable
fun BookMarksTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(56.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = Icons.Default.Search,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Text(
            text = "Bookmarks",
            style = MaterialTheme.typography.titleLarge
        )
        Image(
            imageVector = Icons.Default.Settings,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
    }
}