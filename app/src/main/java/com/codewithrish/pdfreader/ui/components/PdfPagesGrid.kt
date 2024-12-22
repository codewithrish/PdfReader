package com.codewithrish.pdfreader.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.designsystem.component.CwrText

@Composable
fun PdfPagesGrid(
    pages: List<Bitmap>,
    selectedItems: List<Int>,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit
) {
    LazyVerticalGrid (
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pages.size) { index ->
            PdfPageItem(
                bitmap = pages[index],
                isSelected = selectedItems.contains(index),
                pageNumber = index + 1,
                onClick = { onItemClick(index) },
                modifier = modifier
            )
        }
    }
}

@Composable
fun PdfPageItem(
    bitmap: Bitmap,
    isSelected: Boolean,
    pageNumber: Int,
    onClick: () -> Unit,
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
                .clickable(onClick = onClick),
        ) {
            Image(
                painter = BitmapPainter(bitmap.asImageBitmap()),
                contentDescription = "PDF Page",
            )
            Image(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selected",
                colorFilter = ColorFilter.tint(selectedColor),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )
            CwrText(
                text = "${if (pageNumber < 10) "0$pageNumber" else pageNumber}",
                fontSize = if (pageNumber < 100) 80.sp else 60.sp,
                color = selectedColor,
                modifier = Modifier
                    //.align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }
    }
}