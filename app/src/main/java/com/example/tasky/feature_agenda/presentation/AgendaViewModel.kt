package com.example.tasky.feature_agenda.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.domain.Result
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val showErrorDialog: Boolean = false,
        val dialogMessage: String = ""
    )

    fun logOutClicked() {
        Log.d(TAG, "logOutClicked redirect user to login page")
        viewModelScope.launch {
            _viewState.emit(AgendaViewState.Loading)
            val result = userRemoteRepository.logOutUser()

            when (result) {
                is Result.Success -> {
                    println("success logout!")
                    // emit a viewState to show LoginScreen composable
                    _viewState.emit(AgendaViewState.Success)
                }

                is Result.Error -> {
                    println("failed logout :(")
                    // emit a viewState to show ErrorMessage
                    _viewState.emit(AgendaViewState.Failure(result.error))
                }
            }
        }
    }
}