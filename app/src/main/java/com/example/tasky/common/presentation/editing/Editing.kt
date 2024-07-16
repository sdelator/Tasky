package com.example.tasky.common.presentation.editing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tasky.R
import com.example.tasky.common.presentation.GrayDivider
import com.example.tasky.common.presentation.LeftCarrotIcon

@Composable
fun EditScreenRoot(navController: NavController) {
    EditScreenContent()
}

@Composable
fun EditScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        Column {
            EditToolbar()
            GrayDivider()
            EditableFieldArea()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableFieldArea() {
    val text = remember { mutableStateOf("") }
    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        singleLine = false,
        placeholder = { Text("Enter the event name...", fontSize = 26.sp) },
        textStyle = TextStyle(fontSize = 26.sp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
        )
    )
}

@Composable
@Preview
fun EditToolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeftCarrotIcon()
        EditTitle()
        SaveButton(onClick = { })
    }
}

@Composable
fun EditTitle() {
    Text(
        text = stringResource(R.string.edit_title),
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    )
}

@Composable
fun SaveButton(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.save),
        color = colorResource(id = R.color.tasky_green),
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    )
}

@Composable
@Preview
fun PreviewEditRootComposable() {
    EditScreenContent()
}