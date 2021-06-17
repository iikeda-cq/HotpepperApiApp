package com.google.codelab.hotpepperapiapp

import com.google.gson.JsonIOException
import retrofit2.HttpException
import java.net.UnknownHostException

data class Failure(
    val message: FailureType,
    val retry: () -> Unit
)

enum class FailureType(val message: Int) {
    NetworkError(R.string.offline_error),
    UnexpectedError(R.string.unexpected_error),
    NotFoundError(R.string.not_fond_error)
}

fun getMessage(e: Throwable): FailureType {
    return when (e) {
        is UnknownHostException -> FailureType.NetworkError
        is JsonIOException -> FailureType.NotFoundError
        else -> FailureType.UnexpectedError
    }
}

