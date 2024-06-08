package com.example.tasky.feature_login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(title: String) {
    Text(
        text = title,
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    )
}

@Composable
fun TextField() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        label = { Text("Enter text") }
    )
}

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonName: String
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(16.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(),
    ) {
        Text(buttonName)
    }
}

@Composable
@Preview
fun LoginScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp)
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(title = "Welcome Back!")
        Spacer(modifier = Modifier.height(16.dp))
        // build textfield
        TextField()
        Spacer(modifier = Modifier.height(16.dp))
        // build textfield
        TextField()
        Spacer(modifier = Modifier.height(16.dp))
        // login button
        Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }, buttonName = "Test")
    }
}