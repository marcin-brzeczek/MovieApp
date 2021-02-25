package com.challenge.omdb.data.api.exceptions

import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.mapToCompliantException(): Throwable =
     when {
        this is UnknownHostException || this is SocketTimeoutException ->
            NoInternetException(cause = cause)

        (this as? HttpException)?.code() == HttpURLConnection.HTTP_UNAUTHORIZED ->
            UnauthorizeException(cause)

        else -> this
    }
