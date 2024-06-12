package com.example.tasky.feature_login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) : ViewModel() {

    companion object {
        const val TAG = "LoginViewModel"
    }

    fun registerUserClicked() {
        Log.d("sandra", TAG)
    }
}