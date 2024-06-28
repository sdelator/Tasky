package com.example.tasky.feature_agenda.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.domain.Result
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
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
    private val _viewEvent = MutableSharedFlow<AgendaViewEvent>()
    val viewEvent: SharedFlow<AgendaViewEvent> = _viewEvent

    // UI changes via Composable
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog

    fun logOutClicked() {
        Log.d(TAG, "logOutClicked redirect user to login page")
        viewModelScope.launch {
            _viewState.emit(AgendaViewState.LoadingSpinner)
            val result = userRemoteRepository.logOutUser()

            when (result) {
                is Result.Success -> {
                    println("success logout!")
                    // emit a viewState to show LoginScreen composable
                    _viewEvent.emit(AgendaViewEvent.NavigateToLoginScreen)
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

    fun getInitials(): String {
        // todo grab user name stored in shared pref and parse firstName.first() lastName.first()
        return "AB"
    }

    fun onErrorDialogDismissed() {
        _showDialog.value = false
    }
}