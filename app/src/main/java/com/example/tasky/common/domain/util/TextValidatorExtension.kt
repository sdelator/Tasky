package com.example.tasky.common.domain.util


fun String.isValidPassword(): Boolean {
    val hasLowerCase = this.any { it.isLowerCase() }
    val hasUpperCase = this.any { it.isUpperCase() }
    val hasDigit = this.any { it.isDigit() }
    val hasValidLength = this.length > 8

    return hasLowerCase && hasUpperCase && hasDigit && hasValidLength
}

fun String.isValidName(): Boolean {
    return this.length in 4..50
}