package com.codewithrish.pdfreader.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews
import com.codewithrish.pdfreader.core.designsystem.component.CwrText
import com.codewithrish.pdfreader.ui.theme.materialColor
import com.codewithrish.pdfreader.ui.theme.materialTextStyle

@Composable
fun EmptyScreenWithText(
    message: String,
    color: Color = materialColor().onBackground,
) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Load the Lottie composition from assets
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("empty_screen1.json"))

        // Create animation progress state
        var isPlaying by remember { mutableStateOf(true) }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying,
            iterations = LottieConstants.IterateForever
        )

        // Render the animation
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(250.dp)
        )

        CwrText(
            text = message,
            style = materialTextStyle().titleMedium.copy(
                fontSize = 18.sp,
                color = Color(0xFF4F4F4F)
            ),
            textAlign = TextAlign.Center,
            color = color,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@CwrPreviews
@Composable
private fun EmptyScreenWithTextPreView() {
    EmptyScreenWithText("Boo! Your bookmarks vanished.\nAdd some to bring them back!")
}