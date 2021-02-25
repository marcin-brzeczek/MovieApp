package com.challenge.omdb

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.HttpURLConnection

internal object ErrorResponseMock {

    data class ErrorStatusResponse(
        val statusCode: Int,
        val error: String
    )

    fun unauthorize() =
        HttpException(
            Response.error<ErrorStatusResponse>(
                HttpURLConnection.HTTP_UNAUTHORIZED,
                """
                {
                  "statusCode": 401,
                  "error": "Unauthorized"
                }
            """.trimIndent().toResponseBody()
            )
        )
}
