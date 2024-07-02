package com.example.tasky.feature_agenda.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.common.presentation.util.ProfileUtils
import com.example.tasky.feature_agenda.domain.repository.AuthenticatedRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val authenticatedRemoteRepository: AuthenticatedRemoteRepository,
    private val sessionStateManager: SessionStateManager
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
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog

    private val _initials = MutableStateFlow<String>("")
    val initials: StateFlow<String> get() = _initials

    private val _monthSelected = MutableStateFlow<String>(getCurrentMonth())
    val monthSelected: StateFlow<String> get() = _monthSelected

    private val _openDatePickerDialog = MutableStateFlow(false)
    val openDatePickerDialog: StateFlow<Boolean> get() = _openDatePickerDialog

    init {
        getProfileInitials()
    }

    private val _showLogoutDropdown = MutableStateFlow(false)
    val showLogoutDropdown: StateFlow<Boolean> get() = _showLogoutDropdown

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
                    _showDialog.value = true
                    _viewState.emit(AgendaViewState.ErrorDialog(result.error))
                }
            }
        }
    }

    fun onErrorDialogDismissed() {
        _showDialog.value = false
    }

    private fun getProfileInitials() {
        val fullName = sessionStateManager.getName()
        if (fullName != null) {
            _initials.value = ProfileUtils.getInitials(fullName)
        }
    }

    fun toggleLogoutDropdownVisibility() {
        _showLogoutDropdown.value = !_showLogoutDropdown.value
    }

    fun toggleOpenDatePickerVisibility() {
        _openDatePickerDialog.value = !_openDatePickerDialog.value
    }

    fun onMonthSelected(month: String) {
        _monthSelected.value = month
    }

    private fun getCurrentMonth(): String {
        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        return dateFormat.format(Date())
    }
}