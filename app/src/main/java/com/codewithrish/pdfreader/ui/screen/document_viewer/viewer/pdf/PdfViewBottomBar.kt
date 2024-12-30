package com.codewithrish.pdfreader.ui.screen.document_viewer.viewer.pdf

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.ui.screen.document_viewer.ViewDocumentUiEvent
import com.codewithrish.pdfreader.ui.screen.document_viewer.ViewDocumentUiState
import com.codewithrish.pdfreader.ui.theme.Shape
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

@Composable
fun PdfViewBottomBar(
    state: ViewDocumentUiState,
    onEvent: (ViewDocumentUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Remember the LazyRow's state
    val lazyListState = rememberLazyListState()

    // Scroll to the selected page whenever it changes
    LaunchedEffect(state.pageNumber.first) {
        lazyListState.animateScrollToItem(state.pageNumber.first)
    }

    LazyRow(
        state = lazyListState,
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
//            .background(materialColor().background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.pages.size) { index ->
            val selected = index == state.pageNumber.first
            val selectedColor = if (selected) Color.Red else Color.LightGray

            CwrCardView (
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                borderColor = selectedColor,
                shape = Shape.noCornerShape,
            ) {
                Box {
                    Image(
                        bitmap = state.pages[index],
                        contentDescription = "Page $index",
                        modifier = Modifier
                            .background(Color.White)
                            .clickable { onEvent(ViewDocumentUiEvent.OnPageChange(Pair(index, state.pages.size))) }
                    )
                    CwrText(
                        text = "${index + 1}",
                        style = materialTextStyle().bodySmall,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(selectedColor)
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}
