package com.challenge.omdb.data.api.exceptions

class NoInternetException(cause: Throwable?) :
    Exception("Check your network connection!", cause)
