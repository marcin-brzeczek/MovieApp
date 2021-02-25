package com.challenge.omdb.data.api

import com.challenge.omdb.data.model.Movie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MovieApi {

//    Search pagination added: https://www.omdbapi.com/?apikey=b9bd48a6&s=Batman&page=2

    //http://www.omdbapi.com/?apikey=b9bd48a6&s=Marvel&type=movie

    //http://www.omdbapi.com/?apikey=b9bd48a6&i=tt4154664

    @GET("/")
    fun searchMovies(
        @QueryMap params: Map<String, String>
    ): Single<SearchMovieResponse>

    @GET("/")
    fun getMovieDetails(
        @QueryMap params: Map<String,String>
    ): Single<Movie>

}
