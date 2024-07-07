package com.example.tasky.feature_agenda.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.common.presentation.util.CalendarHelper
import com.example.tasky.common.presentation.util.ProfileUtils
import com.example.tasky.feature_agenda.domain.repository.AuthenticatedRemoteRepository
import com.vanpra.composematerialdialogs.MaterialDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val authenticatedRemoteRepository: AuthenticatedRemoteRepository,
    private val sessionStateManager: SessionStateManager
) : ViewModel() {

    companion object {
        const val TAG = "AgendaViewModel"
    }

    private val _viewState = MutableStateFlow(AgendaViewState())
    val viewState: StateFlow<AgendaViewState> = _viewState

    private val _viewEvent = Channel<AgendaViewEvent>()
    val viewEvent = _viewEvent.receiveAsFlow()

    // UI does not change
    val initials: String = sessionStateManager.getName()?.let { ProfileUtils.getInitials(it) } ?: ""

    // not displayed on screen; only necessary for business logic
    private val _yearSelected = MutableStateFlow(LocalDate.now().year)
    val yearSelected: StateFlow<Int> get() = _yearSelected

    init {
        viewModelScope.launch {
            _viewState.emit(
                AgendaViewState(
                    calendarDays = CalendarHelper.getCalendarDaysForMonth(
                        LocalDate.now().year,
                        LocalDate.now().monthValue
                    )
                )
            )
        }
    }

    fun logOutClicked() {
        Log.d(TAG, "logOutClicked redirect user to login page")
        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = authenticatedRemoteRepository.logOutUser()

            when (result) {
                is Result.Success -> {
                    println("success logout!")
                    _viewEvent.send(AgendaViewEvent.NavigateToLoginScreen)
                }

                is Result.Error -> {
                    println("failed logout :(")
                    _viewState.update {
                        it.copy(
                            showLoadingSpinner = false,
                            showErrorDialog = true,
                            dataError = result.error
                        )
                    }
                }
            }
        }
    }

    fun onErrorDialogDismissed() {
        _viewState.update {
            it.copy(showErrorDialog = false)
        }
    }

    fun toggleLogoutDropdownVisibility() {
        _viewState.update {
            it.copy(showLogoutDropdown = !_viewState.value.showLogoutDropdown)
        }
    }

    fun updateDateDialogState(dialogState: MaterialDialogState) {
        _viewState.update {
            it.copy(datePickerDialogState = dialogState)
        }
    }

    fun updateDateSelected(month: Int, day: Int, year: Int? = null) {
        // year is only null for horizontal calendar; populated by datePicker on toolbar month selection
        val selectedYear = year ?: _yearSelected.value

        val date = LocalDate.of(selectedYear, month, day)
        val calendarDays =
            if (_viewState.value.monthSelected != date.monthValue || _yearSelected.value != date.year) {
                CalendarHelper.getCalendarDaysForMonth(date.year, date.monthValue)
            } else {
                _viewState.value.calendarDays
            }

        _viewState.update {
            it.copy(
                monthSelected = date.monthValue,
                daySelected = date.dayOfMonth,
                calendarDays = calendarDays
            )
        }
        _yearSelected.value = date.year
    }
}