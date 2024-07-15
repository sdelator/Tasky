package com.example.tasky.actions.presentation

import androidx.lifecycle.ViewModel
import com.example.tasky.common.presentation.LineItemType
import com.vanpra.composematerialdialogs.MaterialDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ActionViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val TAG = "ActionViewModel"
        const val DEFAULT_TIME_RANGE = 15L
    }

    private val _viewState = MutableStateFlow(ActionViewState())
    val viewState: StateFlow<ActionViewState> = _viewState

    init {
        _viewState.update {
            it.copy(
                startTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
                endTime = LocalTime.now().plusMinutes(DEFAULT_TIME_RANGE)
                    .format(DateTimeFormatter.ofPattern("HH:mm")).toString()
            )
        }
    }

    fun cancel() {
    }

    fun save() {
    }

    fun updateDateDialogState(dialogState: MaterialDialogState) {
        _viewState.update {
            it.copy(datePickerDialogState = dialogState)
        }
    }

    fun updateTimeDialogState(dialogState: MaterialDialogState) {
        _viewState.update {
            it.copy(timePickerDialogState = dialogState)
        }
    }

    fun updateTimeSelected(time: LocalTime, lineItemType: LineItemType) {
        when (lineItemType) {
            LineItemType.TO -> {
                _viewState.update {
                    it.copy(startTime = time.toString())
                }
            }

            LineItemType.FROM -> {
                _viewState.update {
                    it.copy(endTime = time.toString())
                }
            }
        }
    }
}