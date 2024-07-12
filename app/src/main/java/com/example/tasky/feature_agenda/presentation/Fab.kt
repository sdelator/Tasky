package com.example.tasky.feature_agenda.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasky.R

@Composable
fun Fab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        Icon(Icons.Filled.Add, stringResource(R.string.floating_action_button))
    }
}

@Composable
@Preview
fun PreviewFab() {
    Fab(onClick = {})
}