package com.example.tasky.common.presentation.editing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tasky.R
import com.example.tasky.common.domain.Constants
import com.example.tasky.common.domain.model.AgendaItemType
import com.example.tasky.common.presentation.GrayDivider
import com.example.tasky.common.presentation.LeftCarrotIcon

@Composable
fun EditScreenRoot(
    textFieldType: String,
    agendaItemType: String,
    navController: NavController,
    editingViewModel: EditingViewModel = hiltViewModel()
) {
    val text by editingViewModel.text.collectAsStateWithLifecycle()
    val fieldType = enumValueOf<TextFieldType>(textFieldType)
    val agendaType = enumValueOf<AgendaItemType>(agendaItemType)

    EditScreenContent(
        navigateToPreviousScreen = { navController.navigateUp() },
        saveText = {
            val textChanged = editingViewModel.formatText(text)
            if (fieldType == TextFieldType.TITLE) {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    Constants.TITLE,
                    textChanged
                )
            } else {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    Constants.DESCRIPTION,
                    textChanged
                )
            }
            navController.navigateUp()
        },
        text = text,
        onTextChange = { editingViewModel.onTextChange(it) },
        toolbarTitle = when (fieldType) {
            TextFieldType.TITLE -> stringResource(R.string.edit_title)
            TextFieldType.DESCRIPTION -> stringResource(id = R.string.edit_description)
        },
        placeholderText = getPlaceholderText(fieldType, agendaType)
    )
}

@Composable
fun EditScreenContent(
    toolbarTitle: String,
    navigateToPreviousScreen: () -> Unit,
    saveText: (String) -> Unit,
    text: String,
    placeholderText: String,
    onTextChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        Column {
            EditToolbar(
                toolbarTitle = toolbarTitle,
                navigateToPreviousScreen = navigateToPreviousScreen,
                saveEventTitle = { saveText(text) }
            )
            GrayDivider()
            EditableFieldArea(
                text = text,
                placeholderText = placeholderText,
                onTextChange = onTextChange
            )
        }
    }
}

@Composable
fun EditableFieldArea(
    text: String,
    placeholderText: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        singleLine = false,
        placeholder = { Text(placeholderText, fontSize = 26.sp) },
        textStyle = TextStyle(fontSize = 26.sp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
fun EditToolbar(
    toolbarTitle: String,
    navigateToPreviousScreen: () -> Unit,
    saveEventTitle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeftCarrotIcon(
            modifier = Modifier.clickable { navigateToPreviousScreen() }
        )
        EditTitle(toolbarTitle = toolbarTitle)
        SaveButton(onClick = { saveEventTitle() })
    }
}

@Composable
fun EditTitle(toolbarTitle: String) {
    Text(
        text = toolbarTitle,
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
        fontSize = 16.sp,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun getPlaceholderText(textFieldType: TextFieldType, agendaItemType: AgendaItemType): String {
    return when (textFieldType) {
        TextFieldType.TITLE -> {
            when (agendaItemType) {
                AgendaItemType.Event -> stringResource(id = R.string.enter_the_event_name)
                AgendaItemType.Task -> stringResource(id = R.string.enter_the_task_name)
                AgendaItemType.Reminder -> stringResource(id = R.string.enter_the_reminder_name)
            }
        }

        TextFieldType.DESCRIPTION -> {
            when (agendaItemType) {
                AgendaItemType.Event -> stringResource(id = R.string.enter_the_event_description)
                AgendaItemType.Task -> stringResource(id = R.string.enter_the_task_description)
                AgendaItemType.Reminder -> stringResource(id = R.string.enter_the_reminder_description)
            }
        }
    }
}

@Composable
@Preview
fun PreviewEditRootComposable() {
    EditScreenContent(
        toolbarTitle = stringResource(id = R.string.edit_title),
        placeholderText = stringResource(R.string.enter_the_event_name),
        navigateToPreviousScreen = { },
        saveText = { },
        onTextChange = { _ -> },
        text = "Test"
    )
}