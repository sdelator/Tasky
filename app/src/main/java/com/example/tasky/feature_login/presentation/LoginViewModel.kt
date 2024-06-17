package com.example.tasky.feature_login.presentation
import androidx.lifecycle.ViewModel
import com.example.tasky.common.domain.isValidEmail
import com.example.tasky.common.domain.isValidName
import com.example.tasky.common.domain.isValidPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    private val _isNameValid = MutableStateFlow(false)
    val isNameValid: StateFlow<Boolean> get() = _isNameValid

    private val _isEmailValid = MutableStateFlow(false)
    val isEmailValid: StateFlow<Boolean> get() = _isEmailValid

    private val _isPasswordValid = MutableStateFlow(false)
    val isPasswordValid: StateFlow<Boolean> get() = _isPasswordValid

    fun onNameChange(newName: String) {
        _name.value = newName
        _isNameValid.value = newName.isValidName()
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _isEmailValid.value = newEmail.isValidEmail()
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _isPasswordValid.value = newPassword.isValidPassword()
    }
}