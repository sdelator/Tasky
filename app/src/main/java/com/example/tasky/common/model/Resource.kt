package com.example.tasky.common.model

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(
        val errorMessage: String? = "",
        val throwable: Throwable? = null
    ) : Resource<T>()

}