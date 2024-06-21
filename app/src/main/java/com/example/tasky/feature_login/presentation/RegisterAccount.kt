package com.example.tasky.feature_login.presentation

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tasky.LoginNav
import com.example.tasky.R
import com.example.tasky.common.domain.isValidName
import com.example.tasky.common.domain.isValidPassword
import com.example.tasky.common.presentation.CreateErrorAlertDialog
import com.example.tasky.common.presentation.Header
import com.example.tasky.common.presentation.SimpleButton
import com.example.tasky.common.presentation.TextBox
import com.example.tasky.feature_login.domain.model.AuthenticationViewState
import com.example.tasky.feature_login.domain.model.RegisterUserInfo


@Composable
fun RegisterAccountContent(navController: NavController) {
    // todo figure out why text fields are not saving w/orientation change
    val loginViewModel: LoginViewModel = hiltViewModel()
    val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
    val uiState by authenticationViewModel.uiState.collectAsState()

    val viewState by authenticationViewModel.viewState.collectAsState()
    val name by loginViewModel.name.collectAsState()
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()

    val isNameValid by loginViewModel.isNameValid.collectAsState()
    val isEmailValid by loginViewModel.isEmailValid.collectAsState()
    val isPasswordValid by loginViewModel.isPasswordValid.collectAsState()

    val isFormValid = isNameValid && isEmailValid && isPasswordValid

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 70.dp)
            .safeDrawingPadding()
    )
    {
        Header(title = stringResource(R.string.create_your_account))
        // White bottom sheet-like shape
        Card(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 100.dp)
                .align(BottomCenter), // Align the card at the bottom center
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
                TextBox(hintText = stringResource(R.string.name),
                    text = name,
                    onValueChange = { loginViewModel.onNameChange(it) },
                    validator = { it.isValidName() }
                )
                TextBox(hintText = stringResource(R.string.email),
                    text = email,
                    onValueChange = { loginViewModel.onEmailChange(it) },
                    validator = { isEmailValid }
                )
                TextBox(
                    hintText = stringResource(R.string.password),
                    text = password,
                    onValueChange = { loginViewModel.onPasswordChange(it) },
                    validator = { it.isValidPassword() },
                    isPasswordField = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                SimpleButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid,
                    onClick = {
                        // clear focus hides the keyboard
                        focusManager.clearFocus()

                        authenticationViewModel.registerUserClicked(
                            RegisterUserInfo(
                                name,
                                email,
                                password
                            )
                        )
                    },
                    buttonName = stringResource(R.string.get_started)
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
                // Handle success by navigating to LoginScreen
                navController.navigate(LoginNav)
            }

            is AuthenticationViewState.Failure -> {
                // Show an Alert Dialog with API failure Error code/message
                val message = (viewState as AuthenticationViewState.Failure).message
                LaunchedEffect(message) {
                    authenticationViewModel.onShowErrorDialog(message)
                }
            }
        }

        if (uiState.showErrorDialog) {
            CreateErrorAlertDialog(
                showDialog = uiState.showErrorDialog,
                dialogMessage = uiState.dialogMessage,
                onDismiss = { authenticationViewModel.onDismissErrorDialog() }
            )
        }
    }
}