package com.codewithrish.pdfreader.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.common.util.DataUnitConverter
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import org.joda.time.DateTime
import timber.log.Timber

@Composable
fun DocumentDetails(
    document: Document,
    noOfPages: Int,
    modifier: Modifier = Modifier,
    onDocumentClick: (Document) -> Unit = {},
    onBookmarkClick: (Long, Boolean) -> Unit = {_, _ -> },
) {
    Column(
        modifier = modifier
            .clickable {
                onDocumentClick(document)
                Timber
                    .tag("DocumentItem")
                    .d(document.toString())
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical =  8.dp)
        ) {
            Image(
                painter = painterResource(
                    id = when (document.mimeType) {
                        DocumentType.PDF.name -> R.drawable.ic_pdf
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
                modifier = Modifier
                    .size(24.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.Start,   // Align text to start
                modifier = Modifier.weight(1f)           // Use remaining space
            ) {
                Text(
                    text = document.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                    fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                )
                Text(
                    text = "${DataUnitConverter.formatDataSize(document.size)}  |  ${DateTime(document.dateTime).toString("dd MMM yyyy")}  |  $noOfPages",
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                )
            }
            Icon(
                imageVector = if (document.bookmarked) Icons.Default.BookmarkAdded else Icons.Default.BookmarkAdd,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp).
                clickable {
                    onBookmarkClick(document.id, !document.bookmarked)
                }
            )
        }
        // Add a HorizontalDivider at the bottom
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
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
            bookmarked = false
        ),
        noOfPages = 10
    )
}