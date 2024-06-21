package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.R

@Composable
fun TextBox(
    hintText: String,
    text: String,
    isValid: Boolean,
    isPasswordVisible: Boolean = false,
    isPasswordField: Boolean = false,
    onPasswordVisibilityClick: () -> Unit = {},
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        label = { Text(hintText) },
        trailingIcon = {
            if (isValid) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_green_checkmark),
                    contentDescription = "Valid",
                    tint = colorResource(id = R.color.tasky_green)
                )
            } else if (isPasswordField) {
                val image = if (isPasswordVisible) {
                    painterResource(id = R.drawable.ic_password_show)
                } else {
                    painterResource(id = R.drawable.ic_password_hide)
                }
                IconButton(onClick = onPasswordVisibilityClick) {
                    Icon(
                        painter = image,
                        contentDescription = if (isPasswordVisible) {
                            stringResource(R.string.hide_password)
                        } else {
                            stringResource(R.string.show_password)
                        },
                        tint = colorResource(id = R.color.gray)
                    )
                }
            }
        },
        visualTransformation = if (isPasswordField && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
@Preview
fun ViewTextField() {
    var name by remember { mutableStateOf("") }

    Column {
        TextBox(
            hintText = "Name",
            text = "",
            isValid = true,
            onValueChange = { name = it }
        )
    }
}