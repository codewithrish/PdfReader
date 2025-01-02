package com.codewithrish.pdfreader.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.designsystem.icon.CwrIcons

@Composable
fun CwrIcon(
    imageVector: ImageVector,
    size: Dp = 24.dp,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
) {
    Box(
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(size)
        )
    }
}

@CwrPreviews
@Composable
private fun CwrIconPreview() {
    CwrIcon(
        imageVector = CwrIcons.Settings
    )
}