package com.codewithrish.pdfreader.ui.screen.document_viewer.viewer.pdf

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.model.home.Document
import com.codewithrish.pdfreader.ui.screen.document_viewer.ViewDocumentUiEvent
import com.codewithrish.pdfreader.ui.screen.document_viewer.ViewDocumentUiState
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.util.sharePdf
import java.io.File

@Composable
fun PdfViewTopBar(
    state: ViewDocumentUiState,
    onEvent: (ViewDocumentUiEvent) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier)
{
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
//            .background(materialColor().background)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start, // Align everything to start
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Arrow aligned to start
        Icon(
            imageVector = CwrIcons.BackArrow,
            contentDescription = "Back",
            tint = materialColor().onBackground,
            modifier = Modifier.clickable { goBack() }
        )

        // Spacer with weight to push the rest to the end
        Spacer(modifier = Modifier.weight(1f))

        // Icons for ViewMode and Share aligned to end
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = CwrIcons.ViewMode,
                contentDescription = "View Mode",
                tint = materialColor().onBackground,
                modifier = Modifier.clickable {
                    onEvent(ViewDocumentUiEvent.ToggleBottomSheet)
                }
            )
            Icon(
                imageVector = CwrIcons.Share,
                contentDescription = "Share",
                tint = materialColor().onBackground,
                modifier = Modifier.clickable {
                    // Trigger the share action when clicked
                    context.sharePdf(state.document)
                }
            )
        }
    }

}
