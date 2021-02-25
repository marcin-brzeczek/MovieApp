package com.challenge.omdb.presentation.movies

private const val INITIAL_MESSAGE = "Find a movie for yourself\nusing the search bar"

sealed class MoviesState(
    val showData: Boolean = false,
    val message: String? = null,
    val showLoader: Boolean? = null,
    val showReloadButton: Boolean = false,
    val showSearchBar: Boolean = true
) {
    object Initial : MoviesState(message = INITIAL_MESSAGE)
    object Loader : MoviesState(showLoader = true)
    object Success : MoviesState(showData = true, showLoader = false)
    data class Error(val errorMessage: String?) : MoviesState(
        message = errorMessage, showLoader = false
    )

    data class ErrorLoadingMore(val errorMessage: String?) : MoviesState(
        message = errorMessage, showLoader = false, showData = true
    )

    data class ErrorNoConnection(val errorMessage: String?) : MoviesState(
        message = errorMessage,
        showReloadButton = true,
        showSearchBar = false,
        showLoader = false
    )
}
