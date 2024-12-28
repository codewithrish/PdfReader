package com.codewithrish.pdfreader.ui.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codewithrish.pdfreader.ui.theme.materialColor

@Composable
fun CwrHorizontalDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(color = materialColor().outlineVariant.copy(alpha = 0.2f))
}