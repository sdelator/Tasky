package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SimpleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonName: String
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier.padding(16.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(),
    ) {
        Text(buttonName)
    }
}

@Composable
@Preview
fun ViewButton() {
    Column {
        SimpleButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ },
            buttonName = "Test"
        )
    }
}