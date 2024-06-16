package com.example.tasky

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tasky.feature_login.presentation.LoginScreenContent
import com.example.tasky.feature_login.presentation.RegisterAccountContent
import com.example.tasky.feature_splash.domain.SplashViewModel
import com.example.tasky.ui.theme.TaskyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()
    private val isLoggedInState = mutableStateOf(false)

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate is called")
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            //TODO add logic to check navigation
            isLoggedInState.value = splashViewModel.isUserLoggedIn()
            false
        }
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val startDestination: Any = if (isLoggedInState.value) {
                LoginNav
            } else RegisterNav

            TaskyTheme {
                NavHost(navController, startDestination = startDestination) {
                    composable<RegisterNav> {
                        RegisterAccountContent(navController = navController)
                    }
                    composable<LoginNav> {
                        LoginScreenContent()
                    }
                    // todo add other screens
                }
            }
        }
    }
}

@Serializable
object RegisterNav

@Serializable
object LoginNav