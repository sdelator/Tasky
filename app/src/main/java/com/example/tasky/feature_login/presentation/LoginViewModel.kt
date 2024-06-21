package com.example.tasky.feature_login.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.data.EmailPatternValidatorImpl
import com.example.tasky.common.domain.isValidName
import com.example.tasky.common.domain.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val emailPatternValidator: EmailPatternValidatorImpl
) : ViewModel() {

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
}