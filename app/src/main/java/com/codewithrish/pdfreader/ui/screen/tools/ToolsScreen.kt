package com.codewithrish.pdfreader.ui.screen.tools

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    state: ToolsUiState,
    onEvent: (ToolsUiEvent) -> Unit,
    onToolClick: (ToolType) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold (
        topBar = {
            ToolsTopBar(
                modifier = Modifier.fillMaxWidth(),
                onSettingsClick = onSettingsClick
            )
        },
        content = { paddingValues ->
            CwrContentBox(paddingValues = PaddingValues(top = paddingValues.calculateTopPadding(), bottom = 0.dp)) {
                ToolsList(
                    onToolClick = onToolClick,
                    modifier = modifier
                )
            }
        }
    )
    TrackScreenViewEvent(screenName = "Tools Screen")
}

@Composable
fun ToolsTopBar(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CwrText(
            text = stringResource(R.string.feature_tools),
            style = materialTextStyle().titleLarge,
            modifier = Modifier.weight(1f)
        )
        Row (
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = CwrIcons.Settings,
                contentDescription = "",
                tint = materialColor().onSurface,
                modifier = Modifier.clickable {
                    onSettingsClick()
                }
            )
        }
    }
}