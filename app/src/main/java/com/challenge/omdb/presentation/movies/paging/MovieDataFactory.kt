package com.challenge.omdb.presentation.movies.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.challenge.omdb.data.model.Movie
import com.challenge.omdb.presentation.movies.MoviesState
import com.challenge.omdb.usecases.SearchMoviesUseCase
import com.challenge.omdb.utils.EMPTY_STRING
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MovieDataFactory @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

    val movieSourceLiveData: MutableLiveData<MovieDataSource> = MutableLiveData()

    private lateinit var movieDataSource: MovieDataSource
    private var query: String = EMPTY_STRING

    fun updateState(newState: MoviesState) {
        movieDataSource.moviesState.value = newState
    }

    fun setQueryString(query: String) {
        this.query = query
    }

    override fun create(): DataSource<Int, Movie> {
        movieDataSource = MovieDataSource(searchMoviesUseCase, compositeDisposable, query)
        movieSourceLiveData.postValue(movieDataSource)
        return movieDataSource
    }

    fun invalidate() {
        movieDataSource.invalidate()
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }
}
