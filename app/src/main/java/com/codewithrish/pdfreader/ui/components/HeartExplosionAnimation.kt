package com.codewithrish.pdfreader.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import timber.log.Timber

// @TODO fix animation sometimes showing same animation twice
@Composable
fun HeartExplosionAnimation(
    key: Int,
    isLiked: Boolean,
    modifier: Modifier = Modifier
) {
    // Skip rendering if key is 0 or not liked
    if (key == 0 || !isLiked) return

    Timber.tag("HeartExplosionAnimation").d("HeartExplosionAnimation triggered with key: $key and isLiked: $isLiked")

    val hearts = List(6) {
        remember(key) { Animatable(0f) to Animatable(0f) } // Recreate animatables on new key
    }
    val alphas = List(6) { remember(key) { Animatable(1f) } }
    val colors = listOf(
        Color.Red, Color.Magenta, Color.Yellow, Color.Cyan, Color.Green, Color.Blue
    )

    LaunchedEffect(key) {
        hearts.forEachIndexed { index, (xAnimatable, yAnimatable) ->
            launch {
                xAnimatable.animateTo(
                    targetValue = (Math.random() * 100 - 50).toFloat(),
                    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
                )
            }
            launch {
                yAnimatable.animateTo(
                    targetValue = -(Math.random() * 100 + 50).toFloat(), // Explosion upwards
                    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
                )
            }
        }
        alphas.forEach { alpha ->
            launch {
                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 800)
                )
            }
        }
    }

    Box(
        modifier = modifier
            .size(56.dp),
        contentAlignment = Alignment.Center
    ) {
        hearts.forEachIndexed { index, (xAnimatable, yAnimatable) ->
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            xAnimatable.value.toInt(),
                            yAnimatable.value.toInt()
                        )
                    }
                    .graphicsLayer(alpha = alphas[index].value)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Exploding Heart",
                    tint = colors[index % colors.size],
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
