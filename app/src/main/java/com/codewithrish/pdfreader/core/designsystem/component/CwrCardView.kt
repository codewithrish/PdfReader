package com.codewithrish.pdfreader.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.ui.theme.Shape
import com.codewithrish.pdfreader.ui.theme.materialColor

@Composable
fun CwrCardView(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = materialColor().surfaceVariant,
    ),
    elevation: CardElevation = CardDefaults.cardElevation(),
    onClick: () -> Unit = {},
    shape: CornerBasedShape = Shape.large,
    borderColor: Color = materialColor().surfaceVariant,  // Added border color
    borderWidth: Dp = 1.dp,           // Added border width
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier
            .clickable {
                onClick()
            },
        colors = colors,
        elevation = elevation,
        shape = shape,
        border = BorderStroke(width = borderWidth, color = borderColor)
    ) {
        content()
    }
}