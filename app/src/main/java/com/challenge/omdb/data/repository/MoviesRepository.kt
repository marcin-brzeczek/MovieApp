package com.challenge.omdb.data.repository

import com.challenge.omdb.BuildConfig
import com.challenge.omdb.data.model.Movie
import com.challenge.omdb.data.api.MovieApi
import com.challenge.omdb.data.api.SearchMovieResponse
import io.reactivex.Single
import javax.inject.Inject

private const val OMDB_PARAM_API_KEY = "apikey"
private const val OMDB_PARAM_TYPE = "type"
private const val OMDB_PARAM_SEARCH = "s"
private const val OMDB_PARAM_PAGE = "page"
private const val OMDB_PARAM_MOVIE_ID = "i"
private const val OMDB_SELCTED_TYPE = "movie"

class MoviesRepository @Inject constructor(
    private val movieApi: MovieApi
) {

    fun searchMovies(title: String, page: Int): Single<SearchMovieResponse> {
        return movieApi.searchMovies(
            mapOf(
                OMDB_PARAM_API_KEY to BuildConfig.OMDB_API_KEY,
                OMDB_PARAM_SEARCH to title,
                OMDB_PARAM_TYPE to OMDB_SELCTED_TYPE,
                OMDB_PARAM_PAGE to page.toString()
            )
        )
    }

    fun getMovieDetails(id: String): Single<Movie> {
        return movieApi.getMovieDetails(
            mapOf(
                OMDB_PARAM_API_KEY to BuildConfig.OMDB_API_KEY,
                OMDB_PARAM_MOVIE_ID to id
            )
        )
    }
}
