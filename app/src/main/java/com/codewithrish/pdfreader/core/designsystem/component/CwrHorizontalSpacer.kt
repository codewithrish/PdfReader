package com.codewithrish.pdfreader.core.designsystem.component

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.ui.theme.materialColor

@Composable
fun CwrHorizontalDivider(
    thickness: Dp = 16.dp,
    color: Color = materialColor().onSurface.copy(alpha = 0.12f),
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}