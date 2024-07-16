package com.example.tasky.actions.presentation

import android.util.Log
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
                fromTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
                toTime = LocalTime.now().plusMinutes(DEFAULT_TIME_RANGE)
                    .format(DateTimeFormatter.ofPattern("HH:mm")).toString()
            )
        }
    }

    fun cancel() {
    }

    fun save() {
    }

    fun updateDateDialogState(dialogState: MaterialDialogState, lineItemType: LineItemType) {
        when (lineItemType) {
            LineItemType.FROM -> {
                _viewState.update {
                    it.copy(fromDatePickerDialogState = dialogState)
                }
            }

            LineItemType.TO -> {
                _viewState.update {
                    it.copy(toDatePickerDialogState = dialogState)
                }
            }
        }
    }

    fun updateTimeDialogState(dialogState: MaterialDialogState, lineItemType: LineItemType) {
        when (lineItemType) {
            LineItemType.FROM -> {
                _viewState.update {
                    it.copy(fromTimeDialogState = dialogState)
                }
            }

            LineItemType.TO -> {
                _viewState.update {
                    it.copy(toTimeDialogState = dialogState)
                }
            }
        }
    }

    fun updateTimeSelected(time: LocalTime, lineItemType: LineItemType) {
        Log.d("sandra", "updateTimeSelected $lineItemType")
        when (lineItemType) {
            LineItemType.FROM -> {
                _viewState.update {
                    it.copy(fromTime = time.toString())
                }
            }

            LineItemType.TO -> {
                _viewState.update {
                    it.copy(toTime = time.toString())
                }
            }

        }
    }
}