package com.codewithrish.pdfreader.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.ui.theme.materialColor

@Composable
fun CustomRadioButton(
    selected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp, // Set the desired size of the RadioButton
    color: Color = materialColor().primary,
    unselectedColor: Color = materialColor().onSurface.copy(alpha = 0.6f),
) {
    Box(
        modifier = modifier
            .size(size)
            .clickable { onClick() }, // Handle click events
        contentAlignment = Alignment.Center // Center the inner circle
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw the outer circle
            drawCircle(
                color = if (selected) color else unselectedColor,
                style = Stroke(width = 2.dp.toPx()) // Outer circle stroke
            )
            // Draw the inner circle if selected
            if (selected) {
                drawCircle(
                    color = color,
                    radius = size.toPx() / 2.5f // Inner circle radius
                )
            }
        }
    }
}