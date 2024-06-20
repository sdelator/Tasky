package com.example.tasky

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
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
                AgendaNav
            } else AuthNavRoute

            TaskyTheme {
                NavHost(navController, startDestination = startDestination) {
                    navigation<AuthNavRoute>(startDestination = LoginNav) {
                        authGraph(navController)
                    }

                    composable<AgendaNav> {
                        // todo create Composable
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Green)
                        )
                    }

                    // todo add other screens
//                    navigation(
//                        startDestination = "agenda",
//                        route = "calendar"
//                    )
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

fun NavGraphBuilder.authGraph(navController: NavController) {
    composable<RegisterNav> {
        RegisterAccountContent(navController = navController)
    }
    composable<LoginNav> {
        LoginScreenContent(navController = navController)
    }
}

@Serializable
object AuthNavRoute

@Serializable
object RegisterNav

@Serializable
object LoginNav

@Serializable
object AgendaNav