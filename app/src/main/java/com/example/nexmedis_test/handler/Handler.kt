package com.example.nexmedis_test.handler

sealed class ResponseState<out T> {
    data class Success<out T>(val data: T) : ResponseState<T>()
    data class Error(val exception: Throwable) : ResponseState<Nothing>()
}

data class BaseLiveDataState<T>(
    val data: T? = null,
    val isLoading: Boolean = true,
    val errorException: Exception? = null
)

