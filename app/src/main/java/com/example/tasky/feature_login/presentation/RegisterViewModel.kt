package com.example.tasky.feature_login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.util.EmailPatternValidatorImpl
import com.example.tasky.common.domain.util.isValidName
import com.example.tasky.common.domain.util.isValidPassword
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val emailPatternValidator: EmailPatternValidatorImpl,
    private val userRemoteRepository: UserRemoteRepository
) : ViewModel() {
    companion object {
        const val TAG = "RegisterViewModel"
    }

    // viewState triggered by API response
    private val _viewState =
        MutableStateFlow<RegisterViewState?>(null)
    val viewState: StateFlow<RegisterViewState?>
        get() = _viewState

    // UI changes via Composable
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> get() = _isPasswordVisible

    val isNameValid = _name.map { name ->
        name.isValidName() // <- Each name emission is mapped to this boolean when it changes
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    val isEmailValid = _email.map { email ->
        emailPatternValidator.isValidEmailPattern(email) // <- Each email emission is mapped to this boolean when it changes
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    val isPasswordValid = _password.map { password ->
        password.isValidPassword() // <- Each password emission is mapped to this boolean when it changes
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> get() = _showDialog

    // viewEvent triggered by API response
    private val _viewEvent = Channel<RegisterViewEvent>()
    val viewEvent = _viewEvent.receiveAsFlow()

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onPasswordVisibilityClick() {
        _isPasswordVisible.value = !isPasswordVisible.value
    }

    fun registerUserClicked() {
        Log.d(
            TAG,
            "registerUserClicked and userInfo is name: ${name.value}, email: ${email.value}, password: ${password.value}"
        )
        viewModelScope.launch {
            _viewState.emit(RegisterViewState.LoadingSpinner)
            val result = userRemoteRepository.registerUser(name.value, email.value, password.value)

            when (result) {
                is Result.Success -> {
                    println("success register!")
                    // emit a viewState to show LoginScreen composable
                    _viewEvent.send(RegisterViewEvent.NavigateToLogin)
                }

                is Result.Error -> {
                    println("failed register :(")
                    // emit a viewState to show ErrorMessage
                    _showDialog.value = true
                    _viewState.emit(RegisterViewState.ErrorDialog(result.error))
                }
            }
        }
    }

    fun onErrorDialogDismissed() {
        _showDialog.value = false
    }

}