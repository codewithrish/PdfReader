package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.DocumentEntity

@Composable
fun DocumentList(
    documents: List<Document>,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(documents) { documentEntity ->
            CwrCardView {
                DocumentListItem(
                    document = documentEntity,
                    onEvent = onEvent,
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
    }
}