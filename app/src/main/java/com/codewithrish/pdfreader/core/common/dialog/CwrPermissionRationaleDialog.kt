package com.codewithrish.pdfreader.core.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CwrPermissionRationaleDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    cancelButtonText: String,
    dismissDialog: () -> Unit,
    oneGrant: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { /* Handle dismiss */ },
        title = { Text(title) },
        text = { Text(description) },
        confirmButton = {
            Button(onClick = {
                oneGrant()
                dismissDialog()
            }) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            OutlinedButton (onClick = dismissDialog) {
                Text(cancelButtonText)
            }
        }
    )
}