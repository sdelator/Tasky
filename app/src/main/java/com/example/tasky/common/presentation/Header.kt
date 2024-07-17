package com.example.tasky.common.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.common.presentation.editing.TextFieldType


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
fun HeaderLargeStrikeThrough(title: String, isChecked: Boolean, textColor: Color) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeaderMedium(title: String, isChecked: Boolean, textColor: Color) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeaderSmall(title: String, modifier: Modifier) {
    Text(
        text = title,
        modifier = modifier,
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
        HeaderMedium(
            title = "HEADLINE MEDIUM",
            isChecked = true,
            textColor = Color.Black
        )
    }
}

@Composable
@Preview
fun ViewHeaderSmall() {
    Column {
        HeaderSmall(title = "HEADLINE SMALL", modifier = Modifier)
    }
}

@Composable
fun TitleSection(title: String, isEditMode: Boolean, onEditClick: (TextFieldType) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 24.dp)
            .clickable { onEditClick(TextFieldType.TITLE) }
    ) {
        CustomCheckbox(isChecked = false, color = Color.Black, size = 20.dp)
        HeaderLargeStrikeThrough(
            title = title,
            isChecked = false,
            textColor = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isEditMode) {
            RightCarrotIcon()
        }
    }
}
