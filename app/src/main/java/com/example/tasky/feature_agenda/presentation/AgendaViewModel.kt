package com.example.tasky.feature_agenda.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.domain.CalendarHelper
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.common.presentation.util.ProfileUtils
import com.example.tasky.feature_agenda.domain.repository.AuthenticatedRemoteRepository
import com.vanpra.composematerialdialogs.MaterialDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val authenticatedRemoteRepository: AuthenticatedRemoteRepository,
    private val sessionStateManager: SessionStateManager,
    private val calendarHelper: CalendarHelper
) : ViewModel() {

    companion object {
        const val TAG = "AgendaViewModel"
    }

    // viewState triggered by API response
    private val _viewState =
        MutableStateFlow<AgendaViewState?>(null)
    val viewState: StateFlow<AgendaViewState?>
        get() = _viewState

    // viewEvent triggered by API response
    private val _viewEvent = Channel<AgendaViewEvent>()
    val viewEvent = _viewEvent.receiveAsFlow()

    // UI changes via Composable
    val initials: String = sessionStateManager.getName()?.let { ProfileUtils.getInitials(it) } ?: ""

    // not displayed on screen; only necessary for business logic
    private val _yearSelected = MutableStateFlow<Int>(LocalDate.now().year)
    val yearSelected: StateFlow<Int> get() = _yearSelected

    init {
        viewModelScope.launch {
            _viewState.emit(
                AgendaViewState.Content(
                    calendarDays = calendarHelper.getCalendarDaysForMonth(
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
            _viewState.emit(AgendaViewState.LoadingSpinner)
            val result = authenticatedRemoteRepository.logOutUser()

            when (result) {
                is Result.Success -> {
                    println("success logout!")
                    // emit a viewState to show LoginScreen composable
                    _viewEvent.send(AgendaViewEvent.NavigateToLoginScreen)
                }

                is Result.Error -> {
                    println("failed logout :(")
                    // emit a viewState to show ErrorMessage
                    _viewState.emit(AgendaViewState.ErrorDialog(result.error))
                }
            }
        }
    }

    fun onErrorDialogDismissed() {
//        _showDialog.value = false
        _viewState.value = null//AgendaViewState.Content
    }
    fun toggleLogoutDropdownVisibility() {
        val currentState = _viewState.value as? AgendaViewState.Content ?: return
        _viewState.value = currentState.copy(showLogoutDropdown = !currentState.showLogoutDropdown)
    }

    fun updateDateDialogState(dialogState: MaterialDialogState) {
        val currentState = _viewState.value as? AgendaViewState.Content ?: return
        _viewState.value = currentState.copy(datePickerDialogState = dialogState)
    }

    fun updateDateSelected(date: LocalDate) {
        val currentState = _viewState.value as? AgendaViewState.Content ?: return

        // if month or year changed then update calendar days for month
        val calendarDays =
            if (currentState.monthSelected != date.monthValue || yearSelected.value != date.year) {
                calendarHelper.getCalendarDaysForMonth(date.year, date.monthValue)
            } else currentState.calendarDays

        // update month, day, year
        _viewState.value = currentState.copy(
            monthSelected = date.monthValue,
            daySelected = date.dayOfMonth,
            calendarDays = calendarDays
        )
        _yearSelected.value = date.year
    }
}