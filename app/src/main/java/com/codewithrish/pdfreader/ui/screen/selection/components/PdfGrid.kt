package com.codewithrish.pdfreader.ui.screen.selection.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.helper.generatePdfThumbnail
import com.codewithrish.pdfreader.ui.screen.selection.SelectDocumentUiEvent
import com.codewithrish.pdfreader.ui.screen.tools.ToolType
import com.codewithrish.pdfreader.ui.theme.materialColor
import java.io.File

@Composable
fun PdfGrid(
    toolType: ToolType,
    pdfs: List<Document>,
    selectedItems: List<Document>,
    goToToolScreen: (ToolType, Document) -> Unit,
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
        items(pdfs) { pdf ->
            PdfGridItem(
                toolType = toolType,
                pdf = pdf,
                isSelected = selectedItems.contains(pdf),
                goToToolScreen = goToToolScreen,
                onEvent = onEvent,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun PdfGridItem(
    toolType: ToolType,
    pdf: Document,
    isSelected: Boolean,
    goToToolScreen: (ToolType, Document) -> Unit,
    onEvent: (SelectDocumentUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = if (isSelected) materialColor().primary else materialColor().outlineVariant

    var thumbnail by remember { mutableStateOf<ImageBitmap?>(null) }

    // Load thumbnail asynchronously
    if (thumbnail == null) {
        LaunchedEffect(pdf) {
            val file = File(pdf.path)
            thumbnail = generatePdfThumbnail(file)
        }
    }

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
                .clickable {
                    if (toolType.multiSelection) {
                        if (isSelected) {
                            onEvent(SelectDocumentUiEvent.UnSelectDocument(pdf))
                        } else {
                            onEvent(SelectDocumentUiEvent.SelectDocument(pdf))
                        }
                    } else {
                        goToToolScreen(toolType, pdf)
                    }
                },
        ) {
            AnimatedVisibility(
                visible = thumbnail != null,
                enter = slideInVertically(
                    initialOffsetY = { -it } // Starts above the screen
                ),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Thumbnail Image
                    Image(
                        bitmap = thumbnail!!,
                        contentDescription = "Thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Check Icon at the Top Start
                    Icon(
                        imageVector = CwrIcons.CheckCircle,
                        contentDescription = "Selected",
                        tint = selectedColor,
                        modifier = Modifier
                            .align(Alignment.TopStart) // Aligns to top start
                            .padding(8.dp)
                    )

                    Image(
                        painter = painterResource(R.drawable.thumb_pdf),
                        modifier = Modifier.size(55.dp)
                            .align(Alignment.Center),
                        contentDescription = null
                    )

                    // View Icon Box at the Bottom End
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd) // Align this Box to the bottom-end of the parent
                            .clipToBounds()
                            .clip(RoundedCornerShape(topStart = 16.dp))
                            .background(selectedColor.copy(alpha = 0.8f)) // Semi-transparent background
                            .padding(8.dp) // Add padding around the Box
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_eye),
                            contentDescription = "View",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp) // Size of the Icon
                                .align(Alignment.Center) // Center the Icon inside the Box
                        )
                    }

                    CwrText(
                        text = pdf.name,
                        modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp),
                        maxLines = 1
                    )
                }
            }

            AnimatedVisibility(
                visible = thumbnail == null
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp).padding(vertical = 64.dp)
                    )
                }
            }
        }
    }
}