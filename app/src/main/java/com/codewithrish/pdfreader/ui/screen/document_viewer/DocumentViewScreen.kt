package com.codewithrish.pdfreader.ui.screen.document_viewer

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codewithrish.pdfreader.core.model.room.DocumentEntity
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent
import com.codewithrish.pdfreader.ui.screen.home.DocumentType
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import kotlinx.serialization.json.Json

@Composable
fun PdfViewScreen(
    documentJson: String
) {
    val documentEntity = Json.decodeFromString<DocumentEntity>(documentJson)

    val pdfState = rememberVerticalPdfReaderState(
        resource = ResourceType.Local (Uri.parse(documentEntity.uri)),
        isZoomEnable = true
    )

    when (documentEntity.mimeType) {
        DocumentType.PDF.name -> {
            VerticalPDFReader(
                state = pdfState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            )
        }
        else  -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ZoomBounceText()
            }
        }
    }

    TrackScreenViewEvent(screenName = "PdfView Screen")
}

@Composable
fun ZoomBounceText() {
    var visible by remember { mutableStateOf(true) }

    // Animate scale with spring for bounce effect
    val scale by animateFloatAsState(
        targetValue = if (visible) 1.5f else 1f, // Zoom in to 1.5x, then return to 1x
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 100,
            easing = FastOutSlowInEasing // Customize easing for the effect
        ), label = ""
    )

    // Toggle visibility (you can trigger this with any event you want)
    LaunchedEffect(visible) {
        kotlinx.coroutines.delay(2000) // Example: Change visibility after 2 seconds
        visible = !visible // Toggle the visibility
    }

    // Wrap the Text inside AnimatedVisibility
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)),
        exit = scaleOut(animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize() // Ensures Box fills available space
                .padding(16.dp) // Add some padding to avoid clipping
        ) {
            Text(
                text = "Unsupported file type",
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .scale(scale) // Apply animated scale
                    .align(Alignment.Center) // Ensures it's centered within the Box
            )
        }
    }
}