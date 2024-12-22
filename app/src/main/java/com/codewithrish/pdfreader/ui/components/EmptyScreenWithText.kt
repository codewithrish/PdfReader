package com.codewithrish.pdfreader.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.ui.theme.titleLargeTextStyle

@Composable
fun EmptyScreenWithText(
    message: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
) {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        CwrText(
            text = message,
            style = titleLargeTextStyle(),
            textAlign = TextAlign.Center,
            color = color
        )
    }
}