package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.example.tasky.common.presentation.model.AgendaDetailsType

@Composable
fun ActionCard(
    cardType: AgendaDetailsType,
    isChecked: Boolean
) {
    var cardColor = CardDefaults.cardColors()
    var textColor = colorResource(id = R.color.dark_gray)
    var headerColor = Color.Black

    when (cardType) {
        AgendaDetailsType.Event -> {
            cardColor = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.event_light_green)
            )
        }

        AgendaDetailsType.Task -> {
            cardColor = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.tasky_green)
            )
            textColor = Color.White
            headerColor = Color.White
        }

        AgendaDetailsType.Reminder -> {
            cardColor = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.reminder_gray)
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = cardColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomCheckbox(isChecked = isChecked, color = headerColor, size = 16.dp)
            HeaderMedium(
                title = "Place Holder Text",
                isChecked = isChecked,
                textColor = headerColor
            )
            Spacer(modifier = Modifier.weight(1f))
            HorizontalEllipsisIcon()
        }
        CardDetails(details = "Amet minim mollit non deserunt", textColor = textColor)
        DateOfAction(date = "Mar 5, 10:30 - Mar 5, 11:00", textColor = textColor)
    }
}

@Composable
fun CustomCheckbox(isChecked: Boolean, color: Color, size: Dp) {
    Icon(
        painter = if (isChecked) {
            painterResource(id = R.drawable.ic_closed_checkbox)
        } else {
            painterResource(id = R.drawable.ic_open_checkbox)
        },
        modifier = Modifier
            .padding(end = 8.dp)
            .size(size),
        contentDescription = stringResource(R.string.checkbox_circle_icon),
        tint = color,

    )
}

@Composable
fun HorizontalEllipsisIcon() {
    Icon(
        painter = painterResource(id = R.drawable.ic_more_horizontal),
        contentDescription = stringResource(R.string.more_options)
    )
}

@Composable
fun CardDetails(details: String, textColor: Color) {
    Text(
        text = details,
        modifier = Modifier.padding(start = 40.dp, end = 30.dp, bottom = 30.dp),
        style = TextStyle(
            fontSize = 14.sp,
            color = textColor
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun DateOfAction(date: String, textColor: Color) {
    Text(
        text = date,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, end = 16.dp, bottom = 10.dp),
        style = TextStyle(
            fontSize = 14.sp,
            color = textColor
        ),
        textAlign = TextAlign.End
    )
}

@Composable
@Preview
fun PreviewEventCard() {
    ActionCard(
        cardType = AgendaDetailsType.Event,
        isChecked = true
    )
}

@Composable
@Preview
fun PreviewTaskCard() {
    ActionCard(
        cardType = AgendaDetailsType.Task,
        isChecked = false
    )
}

@Composable
@Preview
fun PreviewReminderCard() {
    ActionCard(
        cardType = AgendaDetailsType.Reminder,
        isChecked = true
    )
}

@Composable
@Preview
fun PreviewCustomCheckbox() {
    CustomCheckbox(isChecked = true, color = Color.White, size = 16.dp)
}