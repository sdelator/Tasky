package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.tasky.common.domain.model.AgendaItemType

@Composable
fun AgendaCard(
    modifier: Modifier,
    title: String,
    details: String,
    date: String,
    cardType: AgendaItemType,
    isChecked: Boolean,
    toggleAgendaCardDropdownVisibility: () -> Unit
) {
    var cardColor = CardDefaults.cardColors()
    var textColor = colorResource(id = R.color.dark_gray)
    var headerColor = Color.Black
    var tintColor = Color.Black

    when (cardType) {
        AgendaItemType.Event -> {
            cardColor = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.event_light_green)
            )
        }

        AgendaItemType.Task -> {
            cardColor = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.tasky_green)
            )
            textColor = Color.White
            headerColor = Color.White
            tintColor = Color.White
        }

        AgendaItemType.Reminder -> {
            cardColor = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.reminder_gray)
            )
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = cardColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomCheckbox(isChecked = isChecked, color = headerColor, size = 16.dp)
            HeaderMedium(
                title = title,
                isChecked = isChecked,
                textColor = headerColor
            )
            Spacer(modifier = Modifier.weight(1f))
            HorizontalEllipsisIcon(tint = tintColor, toggleAgendaCardDropdownVisibility)
        }
        CardDetails(details = details, textColor = textColor)
        DateOfAction(date = date, textColor = textColor)
    }
}

@Composable
fun CustomCheckbox(isChecked: Boolean, color: Color, size: Dp) {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = if (isChecked) {
                painterResource(id = R.drawable.ic_closed_checkbox)
            } else {
                painterResource(id = R.drawable.ic_open_checkbox)
            },
            modifier = Modifier
                .size(size),
            contentDescription = stringResource(R.string.checkbox_circle_icon),
            tint = color,

            )
    }
}

@Composable
fun HorizontalEllipsisIcon(tint: Color, toggleAgendaCardDropdownVisibility: () -> Unit) {
    IconButton(onClick = { toggleAgendaCardDropdownVisibility() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_more_horizontal),
            contentDescription = stringResource(R.string.more_options),
            tint = tint
        )
    }
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
    AgendaCard(
        modifier = Modifier,
        title = "Place Holder Text",
        details = "Amet minim mollit non deserunt",
        date = "Mar 5, 10:30 - Mar 5, 11:00",
        cardType = AgendaItemType.Event,
        isChecked = true,
        toggleAgendaCardDropdownVisibility = {}
    )
}

@Composable
@Preview
fun PreviewTaskCard() {
    AgendaCard(
        modifier = Modifier,
        title = "Place Holder Text",
        details = "Amet minim mollit non deserunt",
        date = "Mar 5, 10:30 - Mar 5, 11:00",
        cardType = AgendaItemType.Task,
        isChecked = false,
        toggleAgendaCardDropdownVisibility = {}
    )
}

@Composable
@Preview
fun PreviewReminderCard() {
    AgendaCard(
        modifier = Modifier,
        title = "Place Holder Text",
        details = "Amet minim mollit non deserunt",
        date = "Mar 5, 10:30 - Mar 5, 11:00",
        cardType = AgendaItemType.Reminder,
        isChecked = true,
        toggleAgendaCardDropdownVisibility = {}
    )
}

@Composable
@Preview
fun PreviewCustomCheckbox() {
    CustomCheckbox(isChecked = true, color = Color.White, size = 16.dp)
}