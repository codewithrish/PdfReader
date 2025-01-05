package com.codewithrish.pdfreader.core.designsystem.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CwrModalBottomSheet(
    isSheetVisible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    // State to control the visibility of the bottom sheet
    var showSheet by remember { mutableStateOf(isSheetVisible) }

    // When isSheetVisible changes, update the bottom sheet visibility
    LaunchedEffect(isSheetVisible) {
        showSheet = isSheetVisible
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
                showSheet = false
            }
        ) {
            content()
        }
    }
}