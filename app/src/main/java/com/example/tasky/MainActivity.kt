package com.example.tasky

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.tasky.feature_agenda.presentation.AgendaRoot
import com.example.tasky.feature_agenda.presentation.AgendaViewModel
import com.example.tasky.feature_login.presentation.LoginRoot
import com.example.tasky.feature_login.presentation.LoginViewModel
import com.example.tasky.feature_login.presentation.RegisterAccountContent
import com.example.tasky.feature_splash.presentation.SplashViewModel
import com.example.tasky.ui.theme.TaskyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val agendaViewModel: AgendaViewModel by viewModels()
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
                AgendaNav
            } else AuthNavRoute

            TaskyTheme {
                NavHost(navController, startDestination = startDestination) {
                    navigation<AuthNavRoute>(startDestination = LoginNav) {
                        authGraph(navController, loginViewModel)
                    }

                    navigation<CalendarNavRoute>(startDestination = AgendaNav) {
                        calendarGraph(navController, agendaViewModel)
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.authGraph(navController: NavController, loginViewModel: LoginViewModel) {
    composable<RegisterNav> {
        RegisterAccountContent(navController = navController)
    }
    composable<LoginNav> {
        LoginRoot(navController = navController, loginViewModel = loginViewModel)
    }
}

fun NavGraphBuilder.calendarGraph(navController: NavController, agendaViewModel: AgendaViewModel) {
    composable<AgendaNav> {
        AgendaRoot(navController = navController, agendaViewModel = agendaViewModel)
    }
}

// routes
@Serializable
object AuthNavRoute

@Serializable
object CalendarNavRoute

// composable nav
@Serializable
object RegisterNav

@Serializable
object LoginNav

@Serializable
object AgendaNav