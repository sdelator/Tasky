package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.example.tasky.agenda_details.presentation.AttendeeFilter

@Composable
fun SimpleButton(
    buttonName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
            .height(50.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {
        Text(buttonName)
    }
}

@Composable
fun PillButton(
    buttonName: String,
    onClick: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier
) {
    val containerColor = if (isSelected) Color.Black else colorResource(id = R.color.reminder_gray)
    val contentColor = if (isSelected) Color.White else Color.Black

    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            buttonName,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp, end = 16.dp)
        )
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

@Composable
@Preview
fun ViewPillButton() {
    Column {
        PillButton(
            onClick = { /*TODO*/ },
            buttonName = stringResource(id = AttendeeFilter.NOT_GOING.typeName),
            modifier = Modifier
        )
    }
}