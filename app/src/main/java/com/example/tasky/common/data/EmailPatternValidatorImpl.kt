package com.example.tasky.common.data

import android.util.Patterns
import com.example.tasky.common.domain.EmailPatternValidator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailPatternValidatorImpl @Inject constructor() : EmailPatternValidator {

    override fun isValidEmailPattern(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}