package com.codewithrish.pdfreader.ui.screen.tools.split_pdf

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

@Composable
fun SplitPdfContent(
    state: SplitPdfUiState,
    onEvent: (SplitPdfUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    PdfPagesGrid(
        pages = state.pages,
        selectedItems = state.selectedPages,
        onEvent = onEvent,
    )
}

@Composable
fun PdfPagesGrid(
    pages: List<ImageBitmap>,
    selectedItems: List<Int>,
    modifier: Modifier = Modifier,
    onEvent: (SplitPdfUiEvent) -> Unit,
) {
    LazyVerticalGrid (
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pages.size, key = { it }) { index ->
            PdfPageItem(
                bitmap = pages[index],
                isSelected = selectedItems.contains(index),
                index = index,
                onEvent = onEvent,
                modifier = modifier
            )
        }
    }
}

@Composable
fun PdfPageItem(
    bitmap: ImageBitmap,
    isSelected: Boolean,
    index: Int,
    onEvent: (SplitPdfUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = if (isSelected) materialColor().primary else materialColor().outlineVariant
    val pageNumber = index + 1

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
                .clickable(onClick = { onEvent(SplitPdfUiEvent.TogglePageSelection(index)) }),
        ) {
            Image(
                painter = BitmapPainter(bitmap),
                contentDescription = "PDF Page",
            )
            Icon(
                imageVector = CwrIcons.CheckCircle,
                contentDescription = "Selected",
                tint = selectedColor,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clipToBounds()
                    .background(selectedColor.copy(alpha = 0.8f))
                    .padding(horizontal = 12.dp)
                    .align(Alignment.BottomCenter)
            ) {
                CwrText(
                    text = "${if (pageNumber < 10) "0$pageNumber" else pageNumber}",
                    fontSize = if (pageNumber < 100) 40.sp else 40.sp,
                    color = Color.White,
                    style = materialTextStyle().bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                )
                Icon(
                    painter = painterResource(R.drawable.ic_eye),
                    contentDescription = "View",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                        .clickable { onEvent(SplitPdfUiEvent.ViewPage(bitmap)) }
                )
            }
        }
    }
}