package com.example.tasky

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tasky.feature_login.presentation.LoginScreenContent
import com.example.tasky.feature_splash.domain.SplashViewModel
import com.example.tasky.ui.theme.TaskyTheme

class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    companion object {
        private const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("sandra", TAG)
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            //TODO add logic to check navigation
            val isUserLoggedIn = splashViewModel.checkIfUserIsLoggedIn()
            false
        }
        enableEdgeToEdge()
        setContent {
            TaskyTheme {
                LoginScreenContent()
            }
        }
    }
}