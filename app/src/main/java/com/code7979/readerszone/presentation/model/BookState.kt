package com.code7979.readerszone.presentation.model

sealed class BookState<out T>(
    val data: T? = null
) {
    class Read<T>(data: T) : BookState<T>(data = data)
    class Download<T>(url: T) : BookState<T>(data = url)
}
