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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tasky.AuthNavRoute
import com.example.tasky.CalendarNavRoute
import com.example.tasky.R
import com.example.tasky.RegisterNav
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.presentation.CreateErrorAlertDialog
import com.example.tasky.common.presentation.HeaderLarge
import com.example.tasky.common.presentation.LoadingSpinner
import com.example.tasky.common.presentation.ObserveAsEvents
import com.example.tasky.common.presentation.SimpleButton
import com.example.tasky.common.presentation.TextBox

@Composable
fun LoginRoot(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val viewState by loginViewModel.viewState.collectAsStateWithLifecycle()

    val email by loginViewModel.email.collectAsStateWithLifecycle()
    val password by loginViewModel.password.collectAsStateWithLifecycle()

    val isEmailValid by loginViewModel.isEmailValid.collectAsStateWithLifecycle()
    val isPasswordValid by loginViewModel.isPasswordValid.collectAsStateWithLifecycle()
    val isPasswordVisible by loginViewModel.isPasswordVisible.collectAsStateWithLifecycle()

    val isFormValid = isEmailValid && isPasswordValid

    val showDialog by loginViewModel.showDialog.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    LoginScreenContent(
        email = email,
        password = password,
        isEmailValid = isEmailValid,
        isPasswordValid = isPasswordValid,
        isPasswordVisible = isPasswordVisible,
        isFormValid = isFormValid,
        onEmailChange = { loginViewModel.onEmailChange(it) },
        onPasswordChange = { loginViewModel.onPasswordChange(it) },
        onPasswordVisibilityClick = { loginViewModel.onPasswordVisibilityClick() },
        onLoginClick = {
            focusManager.clearFocus()
            loginViewModel.logInClicked()
        },
        onSignUpClick = { loginViewModel.onSignUpClick() }
    )

    when (viewState) {
        is AuthenticationViewState.LoadingSpinner -> {
            // Show a loading indicator
            LoadingSpinner()
        }

        is AuthenticationViewState.ErrorDialog -> {
            // Show an Alert Dialog with API failure Error code/message
            val message =
                (viewState as AuthenticationViewState.ErrorDialog).dataError.toLoginErrorMessage(
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

    ObserveAsEvents(flow = loginViewModel.viewEvent) { event ->
        when (event) {
            is LoginViewEvent.NavigateToAgenda -> {
                navController.navigate(CalendarNavRoute) {
                    popUpTo(AuthNavRoute) {
                        inclusive = true
                    }
                }
            }

            is LoginViewEvent.NavigateToRegister -> {
                navController.navigate(RegisterNav)
            }
        }
    }
}

@Composable
fun LoginScreenContent(
    email: String,
    password: String,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    isPasswordVisible: Boolean,
    isFormValid: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 70.dp)
            .safeDrawingPadding()
    ) {
        HeaderLarge(title = stringResource(R.string.welcome_back))
        // White bottom sheet-like shape
        Card(
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            // Content inside the card
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextBox(
                    hintText = stringResource(R.string.email),
                    text = email,
                    isValid = isEmailValid,
                    onValueChange = onEmailChange
                )
                TextBox(
                    hintText = stringResource(R.string.password),
                    text = password,
                    isValid = isPasswordValid,
                    onValueChange = onPasswordChange,
                    isPasswordField = true,
                    isPasswordVisible = isPasswordVisible,
                    onPasswordVisibilityClick = onPasswordVisibilityClick
                )
                Spacer(modifier = Modifier.height(16.dp))
                SimpleButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid,
                    onClick = onLoginClick,
                    buttonName = stringResource(R.string.log_in)
                )
                Spacer(modifier = Modifier.weight(1f))
                AnnotatedSignUpText(
                    onClick = onSignUpClick
                )
            }
        }
    }
}

fun DataError.toLoginErrorMessage(context: Context): String {
    return when (this) {
        DataError.Network.UNAUTHORIZED -> context.getString(R.string.incorrect_credentials)
        DataError.Network.NO_INTERNET -> context.getString(R.string.no_internet_connection)
        else -> context.getString(R.string.unknown_error)
    }
}

@Composable
fun AnnotatedSignUpText(onClick: () -> Unit) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.LightGray)) {
                append(stringResource(R.string.dont_have_an_account))
            }
            append(" ")
            withLink(
                LinkAnnotation.Clickable(
                    tag = stringResource(R.string.sign_up),
                    style = SpanStyle(color = MaterialTheme.colorScheme.primary),
                    linkInteractionListener = { onClick() }
                )
            ) {
                append(stringResource(R.string.sign_up))
            }
        },
        modifier = Modifier.padding(bottom = 50.dp, top = 50.dp)
    )
}

@Composable
@Preview
fun PreviewLoginContent() {
    LoginScreenContent(
        email = "",
        password = "",
        isEmailValid = true,
        isPasswordValid = false,
        isPasswordVisible = true,
        isFormValid = false,
        onEmailChange = { },
        onPasswordChange = { },
        onPasswordVisibilityClick = { },
        onLoginClick = { },
        onSignUpClick = { }
    )
}

@Composable
@Preview
fun PreviewAnnotatedSignUpText() {
    AnnotatedSignUpText(onClick = { })
}