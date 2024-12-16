package com.codewithrish.pdfreader.ui.screen.home

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.components.DocumentDetails
import com.codewithrish.pdfreader.ui.helper.PdfUtils

@Composable
fun DocumentList(
    documents: List<Document>,
    onDocumentClick: (Document) -> Unit,
    onBookmarkClick: (Long, Boolean) -> Unit = {_, _ -> },
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
//        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(documents) { document ->
            DocumentDetails(
                document = document,
                noOfPages = PdfUtils.getDocumentFromUri(LocalContext.current, Uri.parse(document.uri))?.numberOfPages ?: 0,
                onDocumentClick = onDocumentClick,
                onBookmarkClick = onBookmarkClick,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}