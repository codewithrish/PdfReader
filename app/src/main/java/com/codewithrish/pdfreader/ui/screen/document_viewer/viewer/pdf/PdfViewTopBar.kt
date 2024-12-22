package com.codewithrish.pdfreader.ui.screen.document_viewer.viewer.pdf

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.screen.document_viewer.ViewDocumentUiEvent
import com.codewithrish.pdfreader.ui.screen.document_viewer.ViewDocumentUiState

@Composable
fun PdfViewTopBar(
    state: ViewDocumentUiState,
    onEvent: (ViewDocumentUiEvent) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier)
{
    val context = LocalContext.current

    Row  (
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = CwrIcons.BackArrow,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickable { goBack() }
        )
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable {
                    // Trigger the share action when clicked
                    sharePdf(context, state.document)
                }
            )
        }
    }
}

// Function to share the PDF
fun sharePdf(context: Context, document: Document?) {
    document?.uri?.let {
        val uri = Uri.parse(it)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, document.name)
            type = "application/pdf"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
    }
}