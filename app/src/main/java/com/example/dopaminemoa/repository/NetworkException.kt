package com.example.dopaminemoa.repository

/**
 * 네트워크 통신 중에 발생할 수 있는 예외에 대한 정보를 포함하는 class 입니다.
 */
class NetworkException(override val message: String) : Exception(message)


/**
 * 네트워크 통신 결과를 나타내는 데에 사용되는 class들 입니다.
 */
sealed class Resource<T>(val data: T? = null, val exception: Exception? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(exception: Exception) : Resource<T>(null, exception)
}