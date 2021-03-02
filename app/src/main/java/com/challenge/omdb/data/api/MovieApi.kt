package com.challenge.omdb.data.api

import com.challenge.omdb.data.model.Movie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MovieApi {

    @GET("/")
    fun searchMovies(
        @QueryMap params: Map<String, String>
    ): Single<SearchMovieResponse>

    @GET("/")
    fun getMovieDetails(
        @QueryMap params: Map<String,String>
    ): Single<Movie>

}
