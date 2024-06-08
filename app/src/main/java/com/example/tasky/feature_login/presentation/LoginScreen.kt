package com.example.tasky.feature_login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.common.presentation.Header
import com.example.tasky.common.presentation.SimpleButton
import com.example.tasky.common.presentation.TextBox


@Composable
@Preview
fun LoginScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(title = "Welcome Back!")
        Column {

        }
        Spacer(modifier = Modifier.height(16.dp))
        // build textfield
        TextBox(hintText = "Email")
        TextBox(hintText = "Password")
        // login button
        Spacer(modifier = Modifier.height(16.dp))
        SimpleButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ },
            buttonName = "Test"
        )
    }
}