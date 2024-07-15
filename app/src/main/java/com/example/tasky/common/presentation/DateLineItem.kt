package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.time.LocalTime

@Composable
fun DateLineItem(
    isEditing: Boolean,
    dialogState: MaterialDialogState,
    timeDialogState: MaterialDialogState,
    updateDateDialogState: (MaterialDialogState) -> Unit,
    updateTimeDialogState: (MaterialDialogState) -> Unit,
    updateTimeSelected: (LocalTime, LineItemType) -> Unit,
    lineItemType: LineItemType,
    time: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (lineItemType == LineItemType.TO) stringResource(id = R.string.from)
            else stringResource(id = R.string.to),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        CustomTimePicker(
            modifier = Modifier.weight(1f),
            dialogState = timeDialogState,
            updateTimeDialogState = updateTimeDialogState,
            updateTimeSelected = updateTimeSelected,
            lineItemType = lineItemType,
            time = time
        )

        if (isEditing) RightCarrotIcon(modifier = Modifier.weight(1f))
        else InvisibleRightCarrotIcon(modifier = Modifier.weight(1f))

        DatePickerLineItem(
            dialogState = dialogState,
            modifier = Modifier
                .weight(2f)
                .padding(start = 16.dp),
            updateDateDialogState = updateDateDialogState
        )

        if (isEditing) RightCarrotIcon()
        else InvisibleRightCarrotIcon()
    }
}

//@Composable
//@Preview
//fun PreviewDateLineItem() {
//    Column {
//        DateLineItem(
//            text = stringResource(id = R.string.to),
//            isEditing = true,
//            dialogState = MaterialDialogState(),
//            timeDialogState = MaterialDialogState(),
//            updateDateDialogState = { },
//            updateTimeDialogState = { },
//            updateTimeSelected = { },
//            timeType = TimeType.TO,
//            time = ""
//        )
//        DateLineItem(
//            text = stringResource(id = R.string.from),
//            isEditing = false,
//            dialogState = MaterialDialogState(),
//            timeDialogState = MaterialDialogState(),
//            updateDateDialogState = { },
//            updateTimeDialogState = { },
//            updateTimeSelected = { },
//            timeType = TimeType.FROM,
//            time = ""
//        )
//    }
//}