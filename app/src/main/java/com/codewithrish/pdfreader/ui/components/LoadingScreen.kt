package com.codewithrish.pdfreader.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        DotsCollisionWithLetters()
    }
}

@CwrPreviews
@Composable
fun LottiePreview() {
    LoadingScreen()
}