package com.example.tasky.common.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasky.R

@Composable
fun CreateErrorAlertDialog(
    showDialog: Boolean,
    dialogMessage: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.error)) },
            text = { Text(text = dialogMessage) },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterAccountContent() {
    val showDialog = true
    val dialogMessage = stringResource(R.string.email_is_already_in_use)

    CreateErrorAlertDialog(
        showDialog = showDialog,
        dialogMessage = dialogMessage,
        onDismiss = {}
    )
}