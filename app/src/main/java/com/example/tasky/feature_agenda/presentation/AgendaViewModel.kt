package com.example.tasky.feature_agenda.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.TaskyState
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository,
    private val taskyState: TaskyState
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

    init {
        getInitials()
    }

    fun logOutClicked() {
        Log.d(TAG, "logOutClicked redirect user to login page")
        viewModelScope.launch {
            _viewState.emit(AgendaViewState.LoadingSpinner)
            val result = userRemoteRepository.logOutUser()

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

    fun getInitials() {
        viewModelScope.launch {
            val fullName = taskyState.getName()
            val nameSplit = fullName.split(" ")
            var initials = ""

            if (nameSplit.size == 1) {
                val name = nameSplit.first()
                initials = "${name[0]}${name[1]}"
            } else {
                for (i in nameSplit.indices) {
                    if (i == 0 || i == nameSplit.lastIndex) {
                        initials += nameSplit[i].first()
                    }
                }
            }
            _initials.value = initials.uppercase()
        }
    }

    fun onErrorDialogDismissed() {
        _showDialog.value = false
    }
}