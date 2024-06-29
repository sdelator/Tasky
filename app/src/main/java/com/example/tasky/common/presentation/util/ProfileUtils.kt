package com.example.tasky.common.presentation.util

object ProfileUtils {
    fun getInitials(fullName: String): String {
        val nameSplit = fullName.split(" ")
        return when {
            nameSplit.size == 1 -> {
                val name = nameSplit.first()
                "${name[0]}${name[1]}".uppercase()
            }

            else -> {
                var initials = ""
                initials += "${nameSplit[0][0]}${nameSplit[nameSplit.lastIndex][0]}"
                initials.uppercase()
            }
        }
    }
}