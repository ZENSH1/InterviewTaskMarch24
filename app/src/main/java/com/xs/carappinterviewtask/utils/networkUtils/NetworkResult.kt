package com.xs.carappinterviewtask.utils.networkUtils

sealed class NetworkResult<T>(
    val result: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
    class Idle<T> : NetworkResult<T>()
}