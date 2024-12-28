package com.codewithrish.pdfreader.core.designsystem.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CwrButton(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        enabled = enabled,
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(
            text = text,
        )
    }
}