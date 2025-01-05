package com.codewithrish.pdfreader.ui.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.codewithrish.pdfreader.ui.theme.materialColor

@Composable
fun CwrHorizontalDivider(
    color: Color = materialColor().outlineVariant.copy(alpha = 0.2f),
    modifier: Modifier = Modifier
) {
    HorizontalDivider(color = color)
}