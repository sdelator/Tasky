package com.example.tasky.common.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun RightCarrotIcon() {
    Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = null,
        tint = Color.Black
    )
}

@Composable
@Preview
fun InvisibleRightCarrotIcon() {
    Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = null,
        tint = Color.Transparent
    )
}