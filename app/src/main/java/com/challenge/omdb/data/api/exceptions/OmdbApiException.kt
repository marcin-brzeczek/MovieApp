package com.challenge.omdb.data.api.exceptions

open class OmdbApiException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception()
