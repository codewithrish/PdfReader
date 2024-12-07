package com.codewithrish.pdfreader.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.common.util.DataUnitConverter
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import kotlinx.serialization.json.Json
import org.joda.time.DateTime
import timber.log.Timber

@Composable
fun DocumentListItem(
    document: DocumentEntity,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable {
                val jsonString = Json.encodeToString(DocumentEntity.serializer(), document)
                onEvent(HomeUiEvent.OpenDocument(jsonString))
                Timber.tag("DocumentItem").d(document.toString())
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.size(40.dp).weight(.1f)
            )
            Spacer(modifier = Modifier.wrapContentSize().width(16.dp))
            Column(
                modifier = Modifier.weight(.9f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = document.name,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    Text(text = DateTime(document.dateTime).toString("dd/MM/yyyy"),
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,)
                    Text(text = " . ",
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,)
                    Text(text = DataUnitConverter.formatDataSize(document.size),
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,)
                }
            }
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background (Color.LightGray).alpha(0.1f))
    }
}

@CwrPreviews
@Composable
private fun DocumentItemPreview() {
    DocumentListItem(
        DocumentEntity(
            id = 1,
            path = "path",
            uri = "uri",
            name = "Rishabh Resume.pdf",
            dateTime = 123456789,
            mimeType = DocumentType.PPT.name,
            size = 123456789,
            bookmarked = false
        ),
        onEvent = {},
        modifier = Modifier
    )
}
