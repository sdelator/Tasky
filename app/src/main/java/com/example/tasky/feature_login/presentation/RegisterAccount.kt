package com.example.tasky.feature_login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tasky.LoginNav
import com.example.tasky.R
import com.example.tasky.common.domain.isValidEmail
import com.example.tasky.common.domain.isValidName
import com.example.tasky.common.domain.isValidPassword
import com.example.tasky.common.presentation.Header
import com.example.tasky.common.presentation.SimpleButton
import com.example.tasky.common.presentation.TextBox
import com.example.tasky.feature_login.domain.model.AuthenticationViewState
import com.example.tasky.feature_login.domain.model.RegisterUserInfo


@Composable
@Preview
fun RegisterAccountContent(navController: NavController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val viewState by loginViewModel.viewState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isNameValid by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }

    val isFormValid = isNameValid && isEmailValid && isPasswordValid

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 70.dp)
    )
    {
        Header(title = stringResource(R.string.create_your_account))
        // White bottom sheet-like shape
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
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
                    onValueChange = {
                        name = it
                        isNameValid = it.isValidName()
                    },
                    validator = { it.isValidName() }
                )
                TextBox(hintText = stringResource(R.string.email),
                    onValueChange = {
                        email = it
                        isEmailValid = it.isValidEmail()
                    },
                    validator = { it.isValidEmail() }
                )
                TextBox(
                    hintText = stringResource(R.string.password),
                    onValueChange = {
                        password = it
                        isPasswordValid = it.isValidPassword()
                    },
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

                        loginViewModel.registerUserClicked(
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
                    dialogMessage = message
                    showDialog = true
                }
            }
        }


        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Error") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}