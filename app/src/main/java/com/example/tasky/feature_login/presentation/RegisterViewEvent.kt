package com.example.tasky.feature_login.presentation

sealed class RegisterViewEvent {
    data object NavigateToLogin : RegisterViewEvent()
}