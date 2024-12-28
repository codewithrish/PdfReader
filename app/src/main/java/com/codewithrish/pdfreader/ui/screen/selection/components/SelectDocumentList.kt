package com.codewithrish.pdfreader.ui.screen.selection.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.common.util.DataUnitConverter
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.components.CwrHorizontalDivider
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import com.codewithrish.pdfreader.ui.screen.selection.SelectDocumentUiEvent
import com.codewithrish.pdfreader.ui.screen.tools.ToolType
import com.codewithrish.pdfreader.ui.theme.PdfReaderTheme
import com.codewithrish.pdfreader.ui.theme.greenTick
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle
import org.joda.time.DateTime

@Composable
fun SelectDocumentList(
    toolType: ToolType,
    pdfs: List<Document>,
    selectedItems: List<Document>,
    goToToolScreen: (ToolType, Document) -> Unit,
    onEvent: (SelectDocumentUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
//        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(pdfs.size) { index ->
            SelectDocumentItem(
                toolType = toolType,
                pdf = pdfs[index],
                isSelected = selectedItems.contains(pdfs[index]),
                goToToolScreen = goToToolScreen,
                onEvent = onEvent,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun SelectDocumentItem(
    toolType: ToolType,
    pdf: Document,
    isSelected: Boolean,
    goToToolScreen: (ToolType, Document) -> Unit,
    onEvent: (SelectDocumentUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .background(if (isSelected) materialColor().surfaceVariant else Color.Transparent)
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (toolType.multiSelection) {
                Icon(
                    imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = null,
                    tint = if (isSelected) greenTick else materialColor().onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
            // Thumbnail Icon
            Image(
                painter = painterResource(
                    id = when (pdf.mimeType) {
                        DocumentType.PDF.name -> if (pdf.isLocked) R.drawable.ic_pdf_locked else R.drawable.ic_pdf
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
                    text = pdf.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = materialTextStyle().bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                CwrText(
                    text = "${DataUnitConverter.formatDataSize(pdf.size)}  |  ${DateTime(pdf.dateTime).toString("dd MMM yyyy")}",
                    style = materialTextStyle().bodySmall.copy(
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        CwrHorizontalDivider()
    }
}

@CwrPreviews
@Composable
private fun SelectDocumentItemPreview() {
    PdfReaderTheme {
        SelectDocumentItem(
            toolType = ToolType.SPLIT_PDF,
            pdf = Document(
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
            isSelected = true,
            goToToolScreen = { _, _ -> },
            onEvent = {}
        )
    }
}