package com.codewithrish.pdfreader.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
) {
    Scaffold (
        topBar = {
            SettingsTopBar(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                goBack = goBack
            )
        },
        content = { paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        paddingValues
//                        top = paddingValues.calculateTopPadding(),
//                        bottom = paddingValues.calculateBottomPadding()
                    ),
                contentAlignment = Alignment.Center
            ) {
                CwrText("Settings Screen")
            }
        },
        bottomBar = {
            SettingsBottomBar(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.systemBars.asPaddingValues()),
            )
        }
    )
}

@Composable
fun SettingsTopBar(
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
            text = "Settings",
            style = materialTextStyle().titleLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun SettingsBottomBar(
    modifier: Modifier = Modifier
) {

}