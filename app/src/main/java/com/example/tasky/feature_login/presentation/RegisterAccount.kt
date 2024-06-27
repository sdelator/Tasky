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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tasky.R
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.presentation.CreateErrorAlertDialog
import com.example.tasky.common.presentation.Header
import com.example.tasky.common.presentation.LoadingSpinner
import com.example.tasky.common.presentation.SimpleButton
import com.example.tasky.common.presentation.TextBox


@Composable
fun RegisterAccountContent(navController: NavController) {
    val loginViewModel: LoginViewModel = hiltViewModel()

    val viewState by loginViewModel.viewState.collectAsStateWithLifecycle()
    val viewEvent by loginViewModel.viewEvent.observeAsState()

    val name by loginViewModel.name.collectAsStateWithLifecycle()
    val email by loginViewModel.email.collectAsStateWithLifecycle()
    val password by loginViewModel.password.collectAsStateWithLifecycle()

    val isNameValid by loginViewModel.isNameValid.collectAsStateWithLifecycle()
    val isEmailValid by loginViewModel.isEmailValid.collectAsStateWithLifecycle()
    val isPasswordValid by loginViewModel.isPasswordValid.collectAsStateWithLifecycle()
    val isPasswordVisible by loginViewModel.isPasswordVisible.collectAsStateWithLifecycle()

    val isFormValid = isNameValid && isEmailValid && isPasswordValid

    val showDialog by loginViewModel.showDialog.collectAsStateWithLifecycle()

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
                TextBox(
                    hintText = stringResource(R.string.name),
                    text = name,
                    isValid = isNameValid,
                    onValueChange = { loginViewModel.onNameChange(it) }
                )
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

                        loginViewModel.registerUserClicked()
                    },
                    buttonName = stringResource(R.string.get_started)
                )
            }
        }

        when (viewState) {
            is AuthenticationViewState.LoadingSpinner -> {
                // Show a loading indicator
                LoadingSpinner()
            }

            is AuthenticationViewState.ErrorDialog -> {
                // Show an Alert Dialog with API failure Error code/message
                val message =
                    (viewState as AuthenticationViewState.ErrorDialog).dataError.toRegisterErrorMessage(
                        context = LocalContext.current
                    )
                CreateErrorAlertDialog(
                    showDialog = showDialog,
                    dialogMessage = message,
                    onDismiss = { loginViewModel.onErrorDialogDismissed() }
                )
            }

            null -> println("no action")
        }
    }
}

fun DataError.toRegisterErrorMessage(context: Context): String {
    return when (this) {
        DataError.Network.UNAUTHORIZED -> context.getString(R.string.email_is_already_in_use)
        DataError.Network.NO_INTERNET -> context.getString(R.string.no_internet_connection)
        DataError.Network.CONFLICT -> context.getString(R.string.email_is_already_in_use)
        else -> context.getString(R.string.unknown_error)
    }
}