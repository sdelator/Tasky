package com.example.tasky.common.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasky.R

@Composable
fun CreateErrorAlertDialog(
    showDialog: MutableState<Boolean>,
    dialogMessage: String
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(stringResource(R.string.error)) },
            text = { Text(text = dialogMessage) },
            confirmButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterAccountContent() {
    val showDialog = remember { mutableStateOf(true) }
    val dialogMessage = "A user with that email already exists."

    CreateErrorAlertDialog(
        showDialog = showDialog,
        dialogMessage = dialogMessage
    )
}