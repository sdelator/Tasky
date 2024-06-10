package com.example.tasky.common.domain

import android.util.Patterns


fun String.isValidPassword(): Boolean {
    val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}$"
    return this.matches(passwordPattern.toRegex())
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidName(): Boolean {
    return this.length in 4..50
}