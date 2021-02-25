package com.challenge.omdb.data.api

import com.challenge.omdb.data.model.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchMovieResponse(
    @Json(name = "Search") val movies: List<Movie>?,
    @Json(name = "Error") val errorMessage: String?
)
