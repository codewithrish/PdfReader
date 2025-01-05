package com.codewithrish.pdfreader.ui.screen.settings.language_settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.R
import com.codewithrish.pdfreader.core.designsystem.component.CwrContentBox
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.ui.screen.settings.SettingsUiEvent
import com.codewithrish.pdfreader.ui.screen.settings.SettingsUiState
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

@Composable
fun LanguageSettingsScreen(
    state: SettingsUiState = SettingsUiState(),
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
) {
    Scaffold (
        topBar = {
            LanguageSettingsTopBar(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                goBack = goBack
            )
        },
        content = { paddingValues ->
            CwrContentBox(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                LanguageSettingsContent(
                    state = state,
                    onEvent = onEvent
                )
            }
        },
        bottomBar = {
            LanguageSettingsBottomBar(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.systemBars.asPaddingValues()),
            )
        }
    )
}

@Composable
fun LanguageSettingsTopBar(
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = CwrIcons.BackArrow,
            contentDescription = "",
            tint = materialColor().onBackground,
            modifier = Modifier.clickable { goBack() }
        )
        CwrText(
            text = stringResource(R.string.language_settings),
            style = materialTextStyle().titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun LanguageSettingsBottomBar(
    modifier: Modifier = Modifier
) {

}