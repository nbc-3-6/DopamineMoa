package com.example.dopaminemoa.data.remote

class NetworkException(val code: Int, override val message: String) : Exception(message)

sealed class Resource<T>(val data: T? = null, val exception: Exception? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(exception: Exception) : Resource<T>(null, exception)
}