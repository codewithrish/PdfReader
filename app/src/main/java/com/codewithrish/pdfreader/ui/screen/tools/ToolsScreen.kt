package com.codewithrish.pdfreader.ui.screen.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent

enum class ToolType {
    SPLIT_PDF,
    MERGE_PDF,
    IMAGE_TO_PDF
}

data class Tool(
    val image: Int? = null,
    val contentDescription: String? = null,
    val imageSize: Dp = 60.dp,
    val featureName: String,
    val toolType: ToolType,
    val modifier: Modifier = Modifier
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    state: ToolsUiState,
    onEvent: (ToolsUiEvent) -> Unit,
    onToolClick: (ToolType) -> Unit,
    modifier: Modifier = Modifier
) {
    val tools = listOf(
        Tool(image = R.drawable.img_scissor, featureName = "Split Pdf", toolType = ToolType.SPLIT_PDF),
        Tool(image = R.drawable.img_merge, featureName = "Merge Pdf", toolType = ToolType.MERGE_PDF),
        Tool(image = R.drawable.img_img_to_pdf, featureName = "Image To Pdf", toolType = ToolType.IMAGE_TO_PDF),
    )

    Scaffold (
        topBar = {
            ToolsTopBar(modifier = Modifier.fillMaxWidth())
        },
        content = { paddingValues ->
            CwrContentBox(paddingValues = paddingValues) {
                ToolsList(
                    tools = tools,
                    onToolClick = onToolClick,
                    modifier = modifier
                )
                Box (
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CwrText(text = "More Tools Coming Soon...")
                }
            }
        }
    )
    TrackScreenViewEvent(screenName = "Tools Screen")
}

@Composable
fun ToolsTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(56.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = Icons.Default.Search,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        CwrText(
            text = "Tools",
            style = MaterialTheme.typography.titleLarge
        )
        Image(
            imageVector = Icons.Default.Settings,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
    }
}