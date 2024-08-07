package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.common.domain.model.AgendaItemType
import java.time.ZonedDateTime

@Composable
fun AgendaCard(
    agendaItem: AgendaItem,
    modifier: Modifier,
    date: String,
    isTaskChecked: Boolean,
    toggleAgendaCardDropdownVisibility: () -> Unit,
    showAgendaCardDropdown: Boolean,
    onAgendaCardActionClick: (AgendaItem, CardAction) -> Unit,
    visibleAgendaCardDropdownId: String?,
    setVisibleAgendaItemId: (String) -> Unit,
    toggleTaskComplete: () -> Unit,
    setTaskCompleteForAgendaItemId: (String) -> Unit,
    taskCompleteForAgendaItemId: String
) {
    var cardColor = CardDefaults.cardColors()
    var textColor = colorResource(id = R.color.dark_gray)
    var headerColor = Color.Black
    var tintColor = Color.Black

    when (agendaItem.cardType) {
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
            CustomCheckbox(
                agendaItem = agendaItem,
                isTaskChecked = isTaskChecked,
                color = headerColor,
                size = 16.dp,
                toggleTaskComplete = toggleTaskComplete,
                setTaskCompleteForAgendaItemId = setTaskCompleteForAgendaItemId,
                taskCompleteForAgendaItemId = taskCompleteForAgendaItemId
            )
            AgendaCardHeaderMedium(
                agendaItem = agendaItem,
                title = agendaItem.title,
                isTaskChecked = isTaskChecked,
                textColor = headerColor,
                taskCompleteForAgendaItemId = taskCompleteForAgendaItemId
            )
            Spacer(modifier = Modifier.weight(1f))
            HorizontalEllipsisIcon(
                tint = tintColor,
                toggleAgendaCardDropdownVisibility = toggleAgendaCardDropdownVisibility,
                agendaItem = agendaItem,
                onAgendaCardActionClick = onAgendaCardActionClick,
                showAgendaCardDropdown = showAgendaCardDropdown,
                visibleAgendaCardDropdownId = visibleAgendaCardDropdownId,
                setVisibleAgendaItemId = setVisibleAgendaItemId
            )
        }
        CardDetails(details = agendaItem.description ?: "", textColor = textColor)
        DateOfAction(date = date, textColor = textColor)
    }
}

@Composable
fun CustomCheckbox(
    agendaItem: AgendaItem? = null,
    isTaskChecked: Boolean,
    color: Color,
    size: Dp,
    toggleTaskComplete: () -> Unit = {},
    setTaskCompleteForAgendaItemId: (String) -> Unit = { _ -> },
    taskCompleteForAgendaItemId: String = ""
) {
    IconButton(onClick = {
        if (agendaItem != null) {
            if (agendaItem.cardType == AgendaItemType.Task) {
                setTaskCompleteForAgendaItemId(agendaItem.id)
                toggleTaskComplete()
            }
        }
    }) {
        Icon(
            painter = if (isTaskChecked && (taskCompleteForAgendaItemId == agendaItem?.id)) {
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
fun AgendaCardHeaderMedium(
    agendaItem: AgendaItem,
    title: String,
    isTaskChecked: Boolean = false,
    textColor: Color,
    taskCompleteForAgendaItemId: String
) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textDecoration = if (isTaskChecked && (taskCompleteForAgendaItemId == agendaItem.id)) TextDecoration.LineThrough else TextDecoration.None
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun HorizontalEllipsisIcon(
    tint: Color,
    toggleAgendaCardDropdownVisibility: () -> Unit,
    agendaItem: AgendaItem,
    showAgendaCardDropdown: Boolean,
    onAgendaCardActionClick: (AgendaItem, CardAction) -> Unit,
    visibleAgendaCardDropdownId: String?,
    setVisibleAgendaItemId: (String) -> Unit
) {
    Box {
        IconButton(onClick = {
            setVisibleAgendaItemId(agendaItem.id)
            toggleAgendaCardDropdownVisibility()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more_horizontal),
                contentDescription = stringResource(R.string.more_options),
                tint = tint
            )
        }
        CardDropdownRoot(
            agendaItem = agendaItem,
            toggleAgendaCardDropdownVisibility = toggleAgendaCardDropdownVisibility,
            onAgendaCardActionClick = onAgendaCardActionClick,
            showAgendaCardDropdown = showAgendaCardDropdown && (visibleAgendaCardDropdownId == agendaItem.id)
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
        date = "Mar 5, 10:30 - Mar 5, 11:00",
        isTaskChecked = true,
        toggleAgendaCardDropdownVisibility = {},
        agendaItem = AgendaItem.Event(
            "123",
            "Place Holder Text",
            "Amet minim mollit non deserunt",
            ZonedDateTime.now(),
            ZonedDateTime.now(),
            ZonedDateTime.now(),
            "",
            true,
            listOf(),
            listOf()
        ),
        onAgendaCardActionClick = { _, _ -> },
        visibleAgendaCardDropdownId = null,
        setVisibleAgendaItemId = {},
        showAgendaCardDropdown = true,
        toggleTaskComplete = {},
        setTaskCompleteForAgendaItemId = { _ -> },
        taskCompleteForAgendaItemId = ""
    )
}

@Composable
@Preview
fun PreviewTaskCard() {
    AgendaCard(
        modifier = Modifier,
        date = "Mar 5, 10:30 - Mar 5, 11:00",
        isTaskChecked = false,
        toggleAgendaCardDropdownVisibility = {},
        agendaItem = AgendaItem.Task(
            "123",
            "Place Holder Text",
            "Amet minim mollit non deserunt",
            ZonedDateTime.now(),
            ZonedDateTime.now(),
            isDone = false
        ),
        onAgendaCardActionClick = { _, _ -> },
        visibleAgendaCardDropdownId = null,
        setVisibleAgendaItemId = {},
        showAgendaCardDropdown = true,
        toggleTaskComplete = {},
        setTaskCompleteForAgendaItemId = { _ -> },
        taskCompleteForAgendaItemId = ""
    )
}

@Composable
@Preview
fun PreviewReminderCard() {
    AgendaCard(
        modifier = Modifier,
        date = "Mar 5, 10:30 - Mar 5, 11:00",
        isTaskChecked = true,
        toggleAgendaCardDropdownVisibility = {},
        agendaItem = AgendaItem.Reminder(
            "123",
            "Place Holder Text",
            "Amet minim mollit non deserunt",
            ZonedDateTime.now(),
            ZonedDateTime.now()
        ),
        onAgendaCardActionClick = { _, _ -> },
        visibleAgendaCardDropdownId = null,
        setVisibleAgendaItemId = {},
        showAgendaCardDropdown = true,
        toggleTaskComplete = {},
        setTaskCompleteForAgendaItemId = { _ -> },
        taskCompleteForAgendaItemId = ""
    )
}

@Composable
@Preview
fun PreviewCustomCheckbox() {
    CustomCheckbox(
        agendaItem = AgendaItem.Task(
            "",
            "",
            "",
            ZonedDateTime.now(),
            ZonedDateTime.now(),
            true
        ),
        isTaskChecked = true,
        color = Color.White,
        size = 16.dp,
        toggleTaskComplete = {},
        setTaskCompleteForAgendaItemId = { _ -> })
}