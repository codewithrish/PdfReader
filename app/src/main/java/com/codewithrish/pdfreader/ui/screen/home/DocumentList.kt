package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.pdfRedColor

@Composable
fun DocumentList(
    documents: List<Document>,
    onDocumentClick: (Document) -> Unit,
    onBookmarkClick: (Long, Boolean) -> Unit = {_, _ -> },
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            items = documents,
            key = { it.id }
        ) { document ->
            DocumentDetails(
                document = document,
                onDocumentClick = onDocumentClick,
                optionOneClick = onBookmarkClick,
                optionIconSize = 15.dp,
                optionOneIcon = if (document.bookmarked) CwrIcons.BookmarkAdded else CwrIcons.BookmarkAdd,
                optionTwoIcon = CwrIcons.Icon3Dots,
                optionOneIconTint = if (document.bookmarked) pdfRedColor else materialColor().onSurface,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}