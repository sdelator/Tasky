package com.example.tasky.common.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RightCarrotIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = null,
        tint = Color.Black,
        modifier = modifier
    )
}

/* Usage: to maintain same spacing for when event is not in edit mode */
@Composable
fun InvisibleRightCarrotIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = null,
        tint = Color.Transparent,
        modifier = modifier
    )
}