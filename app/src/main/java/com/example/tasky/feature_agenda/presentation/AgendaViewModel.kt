package com.example.tasky.feature_agenda.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.R
import com.example.tasky.agenda_details.presentation.utils.DateTimeHelper
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.common.presentation.util.CalendarHelper
import com.example.tasky.common.presentation.util.ProfileUtils
import com.example.tasky.common.presentation.util.toFormatted_MMMM_dd_yyyy
import com.example.tasky.common.presentation.util.toLong
import com.example.tasky.feature_agenda.domain.repository.AgendaRemoteRepository
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
    private val agendaRemoteRepository: AgendaRemoteRepository,
    private val sessionStateManager: SessionStateManager,
    private val application: Application
) : ViewModel() {

    companion object {
        const val TAG = "AgendaViewModel"
    }

    private val _viewState = MutableStateFlow(AgendaViewState())
    val viewState: StateFlow<AgendaViewState> = _viewState

    private val _viewEvent = Channel<AgendaViewEvent>()
    val viewEvent = _viewEvent.receiveAsFlow()

    val initials: String = sessionStateManager.getName()?.let { ProfileUtils.getInitials(it) } ?: ""

    init {
        viewModelScope.launch {
            _viewState.emit(
                AgendaViewState(
                    calendarDays = CalendarHelper.getCalendarDaysForMonth(
                        LocalDate.now().year,
                        LocalDate.now().monthValue
                    ),
                    headerDateText = application.applicationContext.getString(R.string.today)
                )
            )
        }
        loadAgendaForDay(System.currentTimeMillis())
    }

    private fun loadAgendaForDay(time: Long) {
        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaRemoteRepository.loadAgenda(time)

            when (result) {
                is Result.Success -> {
                    println("success agenda for ${DateTimeHelper.formatEpochToDateString(time)}!")
                    println("response = ${result.data}")
                    val agendaItems = result.data
                    _viewState.update {
                        it.copy(
                            agendaItems = agendaItems,
                            showLoadingSpinner = false
                        )
                    }
                }

                is Result.Error -> {
                    println("failed to load agenda for ${DateTimeHelper.formatEpochToDateString(time)} :(")
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

    fun toggleFabDropdownVisibility() {
        _viewState.update {
            it.copy(showFabDropdown = !_viewState.value.showFabDropdown)
        }
    }

    fun updateDateDialogState(dialogState: MaterialDialogState) {
        _viewState.update {
            it.copy(datePickerDialogState = dialogState)
        }
    }

    fun updateDateSelected(month: Int, day: Int, year: Int? = null) {
        // year is only null for horizontal calendar; populated by datePicker on toolbar month selection
        val selectedYear = year ?: _viewState.value.yearSelected

        val date = LocalDate.of(selectedYear, month, day)
        val calendarDays =
            if (_viewState.value.monthSelected != date.monthValue || _viewState.value.yearSelected != date.year) {
                CalendarHelper.getCalendarDaysForMonth(date.year, date.monthValue)
            } else {
                _viewState.value.calendarDays
            }

        _viewState.update {
            it.copy(
                monthSelected = date.monthValue,
                daySelected = date.dayOfMonth,
                yearSelected = date.year,
                calendarDays = calendarDays,
                headerDateText = getHeaderDateText(date),
                dateSelected = date.toLong()
            )
        }
    }

    fun checkAuthentication() {
        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = authenticatedRemoteRepository.authenticate()

            when (result) {
                is Result.Success -> {
                    println("success authentication!")
                    _viewState.update {
                        it.copy(
                            showLoadingSpinner = false
                        )
                    }
                }

                is Result.Error -> {
                    println("failed authentication :(")
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

    private fun getHeaderDateText(date: LocalDate): String {
        return if (date == LocalDate.now()) {
            application.applicationContext.getString(R.string.today)
        } else {
            date.toFormatted_MMMM_dd_yyyy()
        }
    }

    fun formatTimeOnAgendaCard(fromDate: Long, toDate: Long? = null): String {
        println("fromDate $fromDate")

        val startDate = DateTimeHelper.formatEpochToDateString(fromDate)

        if (toDate != null) {
            val endDate = DateTimeHelper.formatEpochToDateString(toDate)
            return "$startDate - $endDate"
        }
        return startDate
    }
}