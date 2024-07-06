package com.example.tasky.feature_agenda.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.domain.CalendarHelper
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.common.presentation.util.ProfileUtils
import com.example.tasky.feature_agenda.domain.repository.AuthenticatedRemoteRepository
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import com.vanpra.composematerialdialogs.MaterialDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
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

    private val _monthSelected = MutableStateFlow<Int>(LocalDate.now().monthValue)
    val monthSelected: StateFlow<Int> get() = _monthSelected

    private val _daySelected = MutableStateFlow<Int>(LocalDate.now().dayOfMonth)
    val daySelected: StateFlow<Int> get() = _daySelected

    private val _yearSelected = MutableStateFlow<Int>(LocalDate.now().year)
    val yearSelected: StateFlow<Int> get() = _yearSelected

    private val _dateDialogState = MutableStateFlow<MaterialDialogState>(MaterialDialogState())
    val dateDialogState: StateFlow<MaterialDialogState> get() = _dateDialogState

    private val _calendarDays = MutableStateFlow<List<CalendarDay>>(emptyList())
    val calendarDays: StateFlow<List<CalendarDay>> get() = _calendarDays

    init {
        viewModelScope.launch {
            _viewState.emit(AgendaViewState.Content())
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
//                    _showDialog.value = true
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
//    fun toggleLogoutDropdownVisibility() {
////        _showLogoutDropdown.value = !_showLogoutDropdown.value
//    }

    fun updateMonthSelected(month: Int) {
        _monthSelected.value = month
    }

    fun updateDateDialogState(dialogState: MaterialDialogState) {
        _dateDialogState.value = dialogState
    }

    private fun getCurrentMonth(): String {
        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun updateDateSelected(date: LocalDate) {
        _monthSelected.value = date.monthValue
        _daySelected.value = date.dayOfMonth
        _yearSelected.value = date.year
    }

    fun getCalendarDaysForMonth(year: Int, month: Int) {
        viewModelScope.launch {
            _calendarDays.value = calendarHelper.getCalendarDaysForMonth(year, month)
        }
    }
}