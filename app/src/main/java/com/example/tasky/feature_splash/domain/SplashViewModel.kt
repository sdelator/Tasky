package com.example.tasky.feature_splash.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    // TODO check if user has an active session or needs to login
    // todo if user has active session, navigate to the agenda screen, else to login screen

    var userIsLoggedIn = true

    fun checkIfUserIsLoggedIn(): Boolean {
        Log.d("sandra", "splashviewmodel check if user is logged in")
        return userIsLoggedIn
    }
}