package com.example.tasky.actions.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
}