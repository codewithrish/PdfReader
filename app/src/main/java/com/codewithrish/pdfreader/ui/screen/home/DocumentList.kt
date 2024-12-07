package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codewithrish.pdfreader.core.model.room.DocumentEntity

@Composable
fun DocumentList(
    documents: List<DocumentEntity>,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(documents) { document ->
            DocumentListItem(
                document = document,
                onEvent = onEvent,
                modifier = modifier
            )
        }
    }
}