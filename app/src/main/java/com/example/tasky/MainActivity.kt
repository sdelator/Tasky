package com.example.tasky

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.tasky.agenda_details.presentation.AgendaDetailsRoot
import com.example.tasky.agenda_details.presentation.components.PhotoDetailRoot
import com.example.tasky.common.domain.Constants
import com.example.tasky.common.domain.model.AgendaItemType
import com.example.tasky.common.presentation.ReminderNotificationService
import com.example.tasky.common.presentation.editing.EditScreenRoot
import com.example.tasky.feature_agenda.presentation.AgendaRoot
import com.example.tasky.feature_login.presentation.RegisterAccountRoot
import com.example.tasky.feature_splash.presentation.SplashViewModel
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

        val service = ReminderNotificationService(this)
        enableEdgeToEdge()
        setContent {
            val startDestination: Any = if (isLoggedInState.value) {
                AgendaNav
            } else AuthNavRoute

            TaskyTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = startDestination) {
                    navigation<AuthNavRoute>(startDestination = LoginNav) {
                        authGraph(navController, service)
                    }

                    navigation<CalendarNavRoute>(startDestination = AgendaNav) {
                        calendarGraph(navController)
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavController,
    service: ReminderNotificationService
) {
    composable<RegisterNav> {
        RegisterAccountRoot(navController = navController)
    }
    composable<LoginNav> {
        //LoginRoot(navController = navController)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { service.showNotification("test") }) {
                Text(text = "show notification")
            }
        }
    }
}

fun NavGraphBuilder.calendarGraph(navController: NavController) {
    composable<AgendaNav> {
        val refreshData = it.savedStateHandle.get<Boolean>(Constants.REFRESH_DATA) ?: false
        println("refresh? $refreshData")
        AgendaRoot(navController = navController, refreshData = refreshData)
    }
    composable<EventNav> {
        val title = it.savedStateHandle.get<String>(Constants.TITLE)
        val description = it.savedStateHandle.get<String>(Constants.DESCRIPTION)
        val imageAction = it.savedStateHandle.get<String>(Constants.IMAGE_ACTION)
        val args = it.toRoute<EventNav>()
        val date = args.date
        AgendaDetailsRoot(
            navController = navController,
            date = date,
            agendaItemType = AgendaItemType.Event,
            title = title,
            description = description,
            imageAction = imageAction
        )
    }
    composable<TaskNav> {
        val title = it.savedStateHandle.get<String>(Constants.TITLE)
        val description = it.savedStateHandle.get<String>(Constants.DESCRIPTION)
        val imageAction = it.savedStateHandle.get<String>(Constants.IMAGE_ACTION)
        val args = it.toRoute<TaskNav>()
        val date = args.date
        AgendaDetailsRoot(
            navController = navController,
            date = date,
            agendaItemType = AgendaItemType.Task,
            title = title,
            description = description,
            imageAction = imageAction
        )
    }
    composable<ReminderNav> {
        val title = it.savedStateHandle.get<String>(Constants.TITLE)
        val description = it.savedStateHandle.get<String>(Constants.DESCRIPTION)
        val imageAction = it.savedStateHandle.get<String>(Constants.IMAGE_ACTION)
        val args = it.toRoute<ReminderNav>()
        val date = args.date
        AgendaDetailsRoot(
            navController = navController,
            date = date,
            agendaItemType = AgendaItemType.Reminder,
            title = title,
            description = description,
            imageAction = imageAction
        )
    }
    composable<EditingNav> {
        val args = it.toRoute<EditingNav>()
        val textFieldType = args.textFieldType
        val agendaDetailType = args.agendaItemType
        EditScreenRoot(
            navController = navController,
            textFieldType = textFieldType,
            agendaItemType = agendaDetailType
        )
    }
    composable<PhotoDetailNav> {
        val args = it.toRoute<PhotoDetailNav>()
        val imageUriAsString = args.image
        PhotoDetailRoot(
            navController = navController,
            imageUriAsString = imageUriAsString
        )
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

//todo maybe I don't need all these, use ~AgendaDetailsNav instead?
@Serializable
data class EventNav(
    val date: Long,
    val agendaItemType: String,
    val agendaItemId: String? = null,
    val cardAction: String? = null
)

@Serializable
data class TaskNav(
    val date: Long,
    val agendaItemType: String,
    val agendaItemId: String? = null,
    val cardAction: String? = null
)

@Serializable
data class ReminderNav(
    val date: Long,
    val agendaItemType: String,
    val agendaItemId: String? = null,
    val cardAction: String? = null
)

@Serializable
data class EditingNav(val textFieldType: String, val agendaItemType: String)

@Serializable
data class PhotoDetailNav(val image: String)