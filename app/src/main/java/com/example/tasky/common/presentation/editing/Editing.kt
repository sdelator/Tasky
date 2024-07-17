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
import com.example.tasky.common.presentation.GrayDivider
import com.example.tasky.common.presentation.LeftCarrotIcon

@Composable
fun EditScreenRoot(
    textFieldType: String,
    navController: NavController,
    editingViewModel: EditingViewModel = hiltViewModel()
) {
    val text by editingViewModel.text.collectAsStateWithLifecycle()

    EditScreenContent(
        navigateToPreviousScreen = { navController.popBackStack() },
        saveEventTitle = {
            val title = editingViewModel.formatTitle(text)
            navController.previousBackStackEntry?.savedStateHandle?.set(Constants.TITLE, title)
            navController.popBackStack()
        },
        text = text,
        onTextChange = { editingViewModel.onTextChange(it) },
        toolbarTitle = when (textFieldType) {
            TextFieldType.TITLE.name -> stringResource(R.string.edit_title)
            TextFieldType.DESCRIPTION.name -> stringResource(id = R.string.edit_description)
            else -> stringResource(id = R.string.edit_description)
        }
    )
}

@Composable
fun EditScreenContent(
    toolbarTitle: String,
    navigateToPreviousScreen: () -> Unit,
    saveEventTitle: (String) -> Unit,
    text: String,
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
                saveEventTitle = { saveEventTitle(text) }
            )
            GrayDivider()
            EditableFieldArea(
                text = text,
                onTextChange = onTextChange
            )
        }
    }
}

@Composable
fun EditableFieldArea(
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
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
@Preview
fun PreviewEditRootComposable() {
    EditScreenContent(
        toolbarTitle = stringResource(id = R.string.edit_title),
        navigateToPreviousScreen = { },
        saveEventTitle = { },
        onTextChange = { _ -> },
        text = "Test"
    )
}