package com.example.tasky.common.presentation.editing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.example.tasky.common.presentation.GrayDivider
import com.example.tasky.common.presentation.LeftCarrotIcon

@Composable
@Preview
fun EditScreenRoot() {
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
        EditToolbar()
        Spacer(modifier = Modifier.padding(32.dp))
        GrayDivider()
    }
}

@Composable
@Preview
fun EditToolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 16.dp),
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