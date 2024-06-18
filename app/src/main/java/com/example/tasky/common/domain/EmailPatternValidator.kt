package com.example.tasky.common.domain

interface EmailPatternValidator {
    fun isValidEmailPattern(email: String): Boolean
}