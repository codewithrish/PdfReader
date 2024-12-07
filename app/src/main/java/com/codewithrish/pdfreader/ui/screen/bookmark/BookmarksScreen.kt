package com.codewithrish.pdfreader.ui.screen.bookmark

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.codewithrish.pdfreader.core.designsystem.component.CwrPreviews
import com.codewithrish.pdfreader.core.ui.TrackScreenViewEvent

@Composable
fun BookmarksScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Bookmarks",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 32.sp,
        )
    }
    TrackScreenViewEvent(screenName = "Bookmarks Screen")
}

@CwrPreviews
@Composable
fun BookmarksScreenPreview() {
    BookmarksScreen()
}