package com.example.tasky.common.model

import android.content.Context
import androidx.annotation.StringRes

sealed interface ResolvableResource {
    fun resolve(context: Context): String
}

data class ResolvableString(val string: String) : ResolvableResource {
    override fun resolve(context: Context): String {
        return string
    }
}

data class ResolvableStringRes(
    @StringRes val stringRes: Int
) : ResolvableResource {
    override fun resolve(context: Context): String {
        return context.getString(stringRes)
    }
}