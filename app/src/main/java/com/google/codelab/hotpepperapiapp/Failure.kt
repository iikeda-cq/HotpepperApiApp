package com.google.codelab.hotpepperapiapp

import retrofit2.HttpException

data class Failure(
    val message: FailureType,
    val retry: () -> Unit
)

enum class FailureType(val message: Int) {
    NetworkError(R.string.offline_error),
    UnexpectedError(R.string.unexpected_error)
}

fun getMessage(e: Throwable): FailureType {
    if (e is HttpException) {
        return FailureType.UnexpectedError
    }
    return FailureType.NetworkError
}

