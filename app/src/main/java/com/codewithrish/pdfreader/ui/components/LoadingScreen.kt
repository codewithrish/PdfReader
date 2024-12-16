package com.codewithrish.pdfreader.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    // Load the animation
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("open_pdf_loader.json"))

    // Control the animation (e.g., play, pause, restart)
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Loop indefinitely
    )

    Scaffold {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize().padding(it)
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = modifier
            )
        }
    }
}

@CwrPreviews
@Composable
fun LottiePreview() {
    LoadingScreen()
}