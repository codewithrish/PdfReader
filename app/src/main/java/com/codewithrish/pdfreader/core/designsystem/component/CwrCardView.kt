package com.codewithrish.pdfreader.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CwrCardView(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ),
    elevation: CardElevation = CardDefaults.cardElevation(),
//    padding: Dp = 16.dp,
    shape: CornerBasedShape = MaterialTheme.shapes.large,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant,  // Added border color
    borderWidth: Dp = 1.dp,           // Added border width
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        colors = colors,
        elevation = elevation,
        shape = shape,
        border = BorderStroke(width = borderWidth, color = borderColor)
    ) {
        content()
    }
}