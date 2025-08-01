package com.codewithrish.pdfreader.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale

@Composable
fun CwrZoomableImage(
    imageBitmap: ImageBitmap,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier
) {
    // States for zoom and pan
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Gesture detection
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 3f) // Limiting zoom between 0.5x and 3x
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
        )
    }
}