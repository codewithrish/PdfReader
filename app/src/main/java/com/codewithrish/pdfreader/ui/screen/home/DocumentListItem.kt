package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.common.util.DataUnitConverter
import com.codewithrish.pdfreader.core.designsystem.component.CwrCardView
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import org.joda.time.DateTime
import timber.log.Timber

@Composable
fun DocumentListItem(
    document: Document,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable {
//                val jsonString = Json.encodeToString(DocumentEntity.serializer(), document)
                onEvent(HomeUiEvent.OpenDocument(document))
                Timber.tag("DocumentItem").d(document.toString())
            }.
        height(60.dp)
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = when(document.mimeType) {
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
                    .size(60.dp)
            )
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = modifier.fillMaxHeight()
            ) {
                Text(
                    text = document.name.replaceFirstChar { it.uppercase() },
                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                    fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(text = DataUnitConverter.formatDataSize(document.size),
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                        fontFamily = MaterialTheme.typography.labelMedium.fontFamily
                    )
                    Text(text = DateTime(document.dateTime).toString("dd-MM-yyyy"),
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                        fontFamily = MaterialTheme.typography.labelMedium.fontFamily
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DocumentItemPreview() {
    Scaffold {
        CwrCardView(
            Modifier.height(92.dp).fillMaxWidth().padding(it)
        ) {
            DocumentListItem(
                Document(
                    id = 1,
                    path = "path",
                    uri = "uri",
                    name = "Rishabh Resume.pdf",
                    dateTime = 123456789,
                    mimeType = DocumentType.PDF.name,
                    size = 123456789,
                    bookmarked = false
                ),
                onEvent = {},
                modifier = Modifier
            )
        }
    }
}
