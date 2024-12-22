package com.codewithrish.pdfreader.core.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import com.codewithrish.pdfreader.core.designsystem.component.CwrText

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
        title = { CwrText(title) },
        text = { CwrText(description) },
        confirmButton = {
            Button(onClick = {
                oneGrant()
                dismissDialog()
            }) {
                CwrText(confirmButtonText)
            }
        },
        dismissButton = {
            OutlinedButton (onClick = dismissDialog) {
                CwrText(cancelButtonText)
            }
        }
    )
}