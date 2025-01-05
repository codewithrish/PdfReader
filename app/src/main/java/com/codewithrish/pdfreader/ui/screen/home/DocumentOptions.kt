package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.designsystem.component.CwrIcon
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.components.CwrHorizontalDivider
import com.codewithrish.pdfreader.ui.screen.settings.middleItemRoundedCornerShape
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.pdfRedColor

@Composable
fun DocumentOptions(
    document: Document,
    onEvent: (HomeUiEvent) -> Unit,
    shareDocument:(Document) -> Unit = {},
    printDocument:(Document) -> Unit = {},
    deleteDocument:(Document) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column {
        DocumentDetails(
            document = document,
            optionOneIcon = if (document.bookmarked) CwrIcons.BookmarkAdded else CwrIcons.BookmarkAdd,
            optionOneIconTint = if (document.bookmarked) pdfRedColor else materialColor().onSurface,
            optionOneClick = { _, _ ->
                onEvent(HomeUiEvent.OnBookmarkClick(document.id, !document.bookmarked))
            }
        )
        CwrHorizontalDivider()
        DocumentOptionsList(
            document = document,
            shareDocument = shareDocument,
            printDocument = printDocument,
            deleteDocument = deleteDocument,
        )
    }
}

@Composable
fun DocumentOptionsList(
    document: Document,
    shareDocument:(Document) -> Unit = {},
    printDocument:(Document) -> Unit = {},
    deleteDocument:(Document) -> Unit = {},
) {
    LazyColumn {
        item {
            DocumentOptionsItem(options[0], onClick = { shareDocument(document) })
        }
        item {
            DocumentOptionsItem(options[1], onClick = { printDocument(document) })
        }
        item {
            DocumentOptionsItem(options[2], onClick = { deleteDocument(document) })
        }
    }
}

@Composable
fun DocumentOptionsItem(
    documentOption: DocumentOption,
    iconTint: Color = materialColor().onSurfaceVariant,
    shape: RoundedCornerShape = middleItemRoundedCornerShape(),
    onClick: () -> Unit = {},
) {
    Column {
        CwrCardView (
            modifier = Modifier
                .fillMaxWidth(),
            shape = shape,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CwrIcon(
                    imageVector = documentOption.icon,
                    tint = iconTint
                )
                CwrText(
                    text = documentOption.title,
                    color = iconTint,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        CwrHorizontalDivider()
    }
}

val options = listOf(
    DocumentOption(
        title = "Share",
        icon = CwrIcons.Share
    ),
    DocumentOption(
        title = "Print",
        icon = CwrIcons.Print
    ),
    DocumentOption(
        title = "Delete",
        icon = CwrIcons.Delete
    ),
)

data class DocumentOption(
    val title: String,
    val icon: ImageVector,
)

@CwrPreviews
@Composable
private fun DocumentOptionsItemPreview() {
    DocumentOptionsItem(options[0])
}