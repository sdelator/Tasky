package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun HeaderLarge(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        ),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeaderMedium(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeaderSmall(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
@Preview
fun ViewHeaderLarge() {
    Column {
        HeaderLarge(title = "HEADLINE")
    }
}

@Composable
@Preview
fun ViewHeaderMedium() {
    Column {
        HeaderMedium(title = "HEADLINE MEDIUM")
    }
}

@Composable
@Preview
fun ViewHeaderSmall() {
    Column {
        HeaderSmall(title = "HEADLINE SMALL")
    }
}
