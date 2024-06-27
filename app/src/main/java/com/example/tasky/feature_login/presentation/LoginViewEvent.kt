package com.example.tasky.feature_login.presentation

sealed class LoginViewEvent {
    data object NavigateToAgenda : LoginViewEvent()
    data object NavigateToLogin : LoginViewEvent()
}