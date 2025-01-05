package com.codewithrish.pdfreader.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable

@Composable
fun CwrAlertDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    cancelButtonText: String,
    positiveClick: () -> Unit = {},
    negativeClick: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = { /* Handle dismiss */ },
        title = { CwrText(title) },
        text = { CwrText(description) },
        confirmButton = {
            Button(onClick = {
                positiveClick()
            }) {
                CwrText(confirmButtonText)
            }
        },
//        dismissButton = {
//            OutlinedButton (onClick = negativeClick) {
//                CwrText(cancelButtonText)
//            }
//        },
    )
}