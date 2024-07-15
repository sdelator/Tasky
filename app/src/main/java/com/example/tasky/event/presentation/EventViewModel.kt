package com.example.tasky.event.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val TAG = "EventViewModel"
    }

    private val _viewState = MutableStateFlow(EventViewState())
    val viewState: StateFlow<EventViewState> = _viewState

    fun cancelEventCreation() {
    }
}