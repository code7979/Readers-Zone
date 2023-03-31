package com.code7979.readerszone.data

sealed class Resource<out T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(errorMessage: String) : Resource<T>(message = errorMessage)
    class Loading<T> : Resource<T>()
    class Empty<T> : Resource<T>()
}
