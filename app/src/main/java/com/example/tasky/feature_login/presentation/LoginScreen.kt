package com.example.tasky.feature_login.presentation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tasky.AgendaNav
import com.example.tasky.R
import com.example.tasky.common.data.util.DataError
import com.example.tasky.common.presentation.CreateErrorAlertDialog
import com.example.tasky.common.presentation.Header
import com.example.tasky.common.presentation.SimpleButton
import com.example.tasky.common.presentation.TextBox
import com.example.tasky.feature_login.domain.model.AuthenticationViewState


@Composable
fun LoginScreenContent(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val viewState by loginViewModel.viewState.collectAsStateWithLifecycle()

    val email by loginViewModel.email.collectAsStateWithLifecycle()
    val password by loginViewModel.password.collectAsStateWithLifecycle()

    val isEmailValid by loginViewModel.isEmailValid.collectAsStateWithLifecycle()
    val isPasswordValid by loginViewModel.isPasswordValid.collectAsStateWithLifecycle()
    val isPasswordVisible by loginViewModel.isPasswordVisible.collectAsStateWithLifecycle()

    val isFormValid = isEmailValid && isPasswordValid

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 70.dp)
            .safeDrawingPadding()
    ) {
        Header(title = stringResource(R.string.welcome_back))
        // White bottom sheet-like shape
        Card(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 100.dp)
                .align(Alignment.BottomCenter), // Align the card at the bottom center
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            // Content inside the card
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TextBox(
                    hintText = stringResource(R.string.email),
                    text = email,
                    isValid = isEmailValid,
                    onValueChange = { loginViewModel.onEmailChange(it) }
                )
                TextBox(
                    hintText = stringResource(R.string.password),
                    text = password,
                    isValid = isPasswordValid,
                    onValueChange = { loginViewModel.onPasswordChange(it) },
                    isPasswordField = true,
                    isPasswordVisible = isPasswordVisible,
                    onPasswordVisibilityClick = { loginViewModel.onPasswordVisibilityClick() }
                )
                Spacer(modifier = Modifier.height(16.dp))
                SimpleButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid,
                    onClick = {
                        // clear focus hides the keyboard
                        focusManager.clearFocus()

                        loginViewModel.logInClicked()
                    },
                    buttonName = stringResource(R.string.log_in)
                )
            }
        }

        when (viewState) {
            is AuthenticationViewState.Loading -> {
                // Show a loading indicator
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .align(Alignment.Center)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is AuthenticationViewState.Success -> {
                // Handle success by navigating to AgendaScreen
                navController.navigate(AgendaNav)
            }

            is AuthenticationViewState.Failure -> {
                // Show an Alert Dialog with API failure Error code/message
                val message =
                    (viewState as AuthenticationViewState.Failure).dataError.toLoginErrorMessage(
                        context = LocalContext.current
                    )
                LaunchedEffect(message) {
                    loginViewModel.onShowErrorDialog(message)
                }
            }
        }

        if (uiState.showErrorDialog) {
            CreateErrorAlertDialog(
                showDialog = true,
                dialogMessage = uiState.dialogMessage,
                onDismiss = { loginViewModel.onDismissErrorDialog() }
            )
        }
    }
}

fun DataError.toLoginErrorMessage(context: Context): String {
    return if (this == DataError.Network.UNAUTHORIZED) {
        context.getString(R.string.incorrect_credentials)
    } else context.getString(R.string.unknown_error)
}

@Composable
@Preview
private fun ViewLoginScreen() {
    val navController = rememberNavController()
    LoginScreenContent(navController = navController)
}