package com.codewithrish.pdfreader.ui.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrText

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
) {
    Scaffold (
        topBar = {
            SettingsTopBar(
                modifier = modifier.fillMaxWidth(),
                goBack = goBack
            )
        },
        content = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                CwrText("Settings Screen")
            }
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
            .height(56.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.clickable { goBack() }
        )
        CwrText(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}