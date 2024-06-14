package com.example.tasky.common.model

import com.example.tasky.R

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(
        val errorMessage: ResolvableResource? = ResolvableStringRes(R.string.empty_string),
        val throwable: Throwable? = null
    ) : Resource<T>()

}