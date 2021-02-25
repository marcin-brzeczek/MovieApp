package com.challenge.omdb.presentation.details

import com.challenge.omdb.data.model.Movie

sealed class MovieDetailsState(
    open val movie: Movie? = null
) {
    data class Success(override val movie: Movie?) : MovieDetailsState()
    data class Error(val errorMessage: String?) : MovieDetailsState()
}
