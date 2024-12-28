package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.common.util.DataUnitConverter
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.components.HeartExplosionAnimation
import com.codewithrish.pdfreader.ui.theme.Shape
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle
import org.joda.time.DateTime

@Composable
fun DocumentDetails(
    document: Document,
    noOfPages: Int,
    optionTwoIcon: Int = CwrIcons.Icon3Dots,
    modifier: Modifier = Modifier,
    onDocumentClick: (Document) -> Unit = {},
    onBookmarkClick: (Long, Boolean) -> Unit = { _, _ -> },
) {
    var bookmarkOffset by remember { mutableStateOf(Offset.Zero) }
    var animationKey by remember { mutableIntStateOf(0) }
    var previousLikedState by remember { mutableStateOf(document.bookmarked) }

    Box(
        modifier = modifier
            .fillMaxWidth() // Ensure the Box expands to full width
            .clickable {
                onDocumentClick(document)
            }
    ) {
        // Main content Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Thumbnail Icon
            Image(
                painter = painterResource(
                    id = when (document.mimeType) {
                        DocumentType.PDF.name -> if (document.isLocked) R.drawable.ic_pdf_locked else R.drawable.ic_pdf
                        DocumentType.WORD.name -> R.drawable.ic_doc
                        DocumentType.PPT.name -> R.drawable.ic_ppt
                        DocumentType.EXCEL.name -> R.drawable.ic_xls
                        DocumentType.CSV.name -> R.drawable.ic_csv
                        DocumentType.TXT.name -> R.drawable.ic_txt
                        DocumentType.EBOOK.name -> R.drawable.ic_ebook
                        else -> R.drawable.ic_unsupported
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            // Document Details
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                CwrText(
                    text = document.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = materialTextStyle().bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                CwrText(
                    text = "${DataUnitConverter.formatDataSize(document.size)}  |  ${DateTime(document.dateTime).toString("dd MMM yyyy")}  |  $noOfPages pages",
                    style = materialTextStyle().bodySmall.copy(
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            // Bookmark Icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(Shape.small)
                    .background(Color.Transparent)
                    .onGloballyPositioned { coordinates ->
                        // Capture the global position and adjust for the parent layout's position
                        bookmarkOffset = coordinates.positionInRoot()
                    }
                    .clickable(
                        onClick = {
                            val newLikedState = !document.bookmarked
                            onBookmarkClick(document.id, newLikedState)
                            // Trigger the animation only if it's being liked (not unliked)
                            if (newLikedState) {
                                animationKey++
                            }
                            previousLikedState = newLikedState
                        },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(if (document.bookmarked) CwrIcons.BookmarkAdded else CwrIcons.BookmarkAdd),
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
            // More Options Icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(Shape.small)
                    .background(Color.Transparent)
                    .clickable(
                        onClick = { /* Action */ },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(optionTwoIcon),
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        // HeartExplosionAnimation - Positioned absolutely
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight() // Ensure the Box covers the entire layout
        ) {
            HeartExplosionAnimation(
                key = animationKey,
                isLiked = document.bookmarked,
                modifier = Modifier
                    .offset {
                        // Adjust the animation position relative to the parent layout
                        IntOffset(
                            x = bookmarkOffset.x.toInt(),
                            y = 0
                        )
                    }
                    .fillMaxHeight() // Allow animation to expand fully vertically
            )
        }

        // Horizontal Divider
        HorizontalDivider(
            thickness = 1.dp,
            color = materialColor().outlineVariant.copy(alpha = 0.2f),
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DocumentDetailsPreview() {
    DocumentDetails(
        document = Document(
            id = 1,
            path = "path",
            uri = "uri",
            name = "Rishabh Resume.pdf",
            dateTime = 123456789,
            mimeType = DocumentType.PDF.name,
            size = 123456789,
            bookmarked = false,
            isLocked = false,
        ),
        noOfPages = 10
    )
}