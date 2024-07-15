package com.example.tasky.actions.presentation

import androidx.lifecycle.ViewModel
import com.vanpra.composematerialdialogs.MaterialDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ActionViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val TAG = "ActionViewModel"
    }

    private val _viewState = MutableStateFlow(ActionViewState())
    val viewState: StateFlow<ActionViewState> = _viewState

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
}