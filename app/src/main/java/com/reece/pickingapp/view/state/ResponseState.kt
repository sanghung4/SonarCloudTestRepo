package com.reece.pickingapp.view.state

sealed class ResponseState<T>(
    val response: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ResponseState<T>(data)
    class Error<T>(message: String?, data: T? = null) : ResponseState<T>(data, message)
    class Cancelled<T> : ResponseState<T>()
    class Default<T> : ResponseState<T>()
}