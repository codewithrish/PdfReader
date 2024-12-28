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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.screen.selection.SelectDocumentUiEvent
import com.codewithrish.pdfreader.ui.screen.tools.ToolType
import com.codewithrish.pdfreader.ui.theme.materialColor

@Composable
fun ImageGrid(
    toolType: ToolType,
    images: List<Document>,
    selectedItems: List<Document>,
    onEvent: (SelectDocumentUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(images) { image ->
            ImageGridItem(
                toolType = toolType,
                image = image,
                isSelected = selectedItems.contains(image),
                onEvent = onEvent,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun ImageGridItem(
    toolType: ToolType,
    image: Document,
    isSelected: Boolean,
    onEvent: (SelectDocumentUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = if (isSelected) materialColor().primary else materialColor().outlineVariant
    CwrCardView(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = materialColor().surface,
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
                    .data(Uri.parse(image.uri))
                    .crossfade(true)
                    .build(),
                contentDescription = "Image from URI",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Adjust as needed
            )
            if (isSelected) {
                Icon(
                    imageVector = CwrIcons.CheckCircle,
                    contentDescription = "Selected",
                    tint = selectedColor,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}