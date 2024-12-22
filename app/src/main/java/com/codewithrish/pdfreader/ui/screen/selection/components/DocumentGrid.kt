package com.codewithrish.pdfreader.ui.screen.selection.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import com.codewithrish.pdfreader.ui.screen.selection.SelectDocumentUiEvent

@Composable
fun DocumentGrid(
    documents: List<Document>,
    selectedItems: List<Document>,
    onEvent: (SelectDocumentUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(documents.filter {
            it.mimeType == DocumentType.PNG.name ||
                    it.mimeType == DocumentType.JPG.name ||
                    it.mimeType == DocumentType.JPEG.name ||
                    it.mimeType == DocumentType.WEBP.name
        }) { image ->
            ImageGridItem(
                image = image,
                isSelected = selectedItems.contains(image),
                onEvent = onEvent,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ImageGridItem(
    image: Document,
    isSelected: Boolean,
    onEvent: (SelectDocumentUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    CwrCardView(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        borderColor = selectedColor,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable(onClick = {
                    if (isSelected) {
                        onEvent(SelectDocumentUiEvent.UnSelectDocument(image))
                    } else {
                        onEvent(SelectDocumentUiEvent.SelectDocument(image))
                    }
                }),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Uri.parse(image.path))
                    .crossfade(true)
                    .build(),
                contentDescription = "Image from URI",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Adjust as needed
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = selectedColor,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}