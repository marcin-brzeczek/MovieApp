package com.challenge.omdb.data.api.exceptions

class UnauthorizeException(cause: Throwable?) :
    Exception("Unauthorized.\nCheck your API KEY!", cause)
