package com.example.tasky.feature_login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.data.EmailPatternValidatorImpl
import com.example.tasky.common.data.util.Result
import com.example.tasky.common.domain.isValidName
import com.example.tasky.common.domain.isValidPassword
import com.example.tasky.feature_login.data.model.LoginUserInfo
import com.example.tasky.feature_login.data.model.RegisterUserInfo
import com.example.tasky.feature_login.domain.model.AuthenticationViewState
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val emailPatternValidator: EmailPatternValidatorImpl,
    private val userRemoteRepository: UserRemoteRepository
) : ViewModel() {

    companion object {
        const val TAG = "LoginViewModel"
    }

    // viewState triggered by API response
    private val _viewState =
        MutableStateFlow<AuthenticationViewState?>(null)
    val viewState: StateFlow<AuthenticationViewState?>
        get() = _viewState

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val showErrorDialog: Boolean = false,
        val dialogMessage: String = ""
    )

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

    fun logInClicked() {
        Log.d(
            TAG,
            "logInClicked and userCredentials is email: ${email.value} and password: ${password.value}"
        )
        viewModelScope.launch {
            _viewState.emit(AuthenticationViewState.Loading)
            val result = userRemoteRepository.logInUser(LoginUserInfo(email.value, password.value))

            when (result) {
                is Result.Success -> {
                    println("success login!")
                    // emit a viewState to change to agenda composable
                    _viewState.emit(AuthenticationViewState.Success)
                }

                is Result.Error -> {
                    println("failed login :(")
                    // emit a viewState to show ErrorMessage
                    _viewState.emit(AuthenticationViewState.Failure(result.error))
                }
            }
        }
    }

    fun registerUserClicked() {
        Log.d(
            TAG,
            "registerUserClicked and userInfo is name: ${name.value}, email: ${email.value}, password: ${password.value}"
        )
        viewModelScope.launch {
            _viewState.emit(AuthenticationViewState.Loading)
            val result = userRemoteRepository.registerUser(
                RegisterUserInfo(
                    name.value,
                    email.value,
                    password.value
                )
            )

            when (result) {
                is Result.Success -> {
                    println("success register!")
                    // emit a viewState to show LoginScreen composable
                    _viewState.emit(AuthenticationViewState.Success)
                }

                is Result.Error -> {
                    println("failed register :(")
                    // emit a viewState to show ErrorMessage
                    _viewState.emit(AuthenticationViewState.Failure(result.error))
                }
            }
        }
    }

    fun onShowErrorDialog(message: String) {
        _uiState.update { it.copy(showErrorDialog = true, dialogMessage = message) }
    }

    fun onDismissErrorDialog() {
        _uiState.update { it.copy(showErrorDialog = false) }
    }
}