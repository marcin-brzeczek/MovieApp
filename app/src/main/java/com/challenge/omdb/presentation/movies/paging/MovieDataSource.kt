package com.challenge.omdb.presentation.movies.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.challenge.omdb.data.api.exceptions.NoInternetException
import com.challenge.omdb.data.api.exceptions.OmdbApiException
import com.challenge.omdb.data.model.Movie
import com.challenge.omdb.presentation.movies.MoviesState
import com.challenge.omdb.usecases.SearchMoviesUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class MovieDataSource @Inject constructor(
    val searchMoviesUseCase: SearchMoviesUseCase,
    val compositeDisposable: CompositeDisposable,
    var searchQuery: String
) : PageKeyedDataSource<Int, Movie>() {

    val moviesState = MutableLiveData<MoviesState>(
        MoviesState.Initial
    )

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        if (searchQuery.isNotBlank()) {
            moviesState.postValue(MoviesState.Loader)
            compositeDisposable.add(
                searchMoviesUseCase(searchQuery, 1)
                    .subscribeBy(
                        onSuccess = {
                            callback.onResult(it, null, 2)
                            moviesState.postValue(MoviesState.Success)
                        },
                        onError = { error ->
                            error.resolveErrorState()
                            Timber.e(error, "Error fetching movies")
                        })
            )
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        compositeDisposable.add(
            searchMoviesUseCase(searchQuery, params.key)
                .subscribeBy(
                    onSuccess = {
                        callback.onResult(it, params.key + 1)
                        moviesState.postValue(MoviesState.Success)

                    },
                    onError = { error ->
                        error.resolveErrorState(occursWhileLoadingMore = true)
                        Timber.e(error, "Error fetching movies")
                    })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        /*no op*/
    }

    private fun Throwable.resolveErrorState(occursWhileLoadingMore: Boolean = false) {
        moviesState.value = when {
            this is OmdbApiException && occursWhileLoadingMore ->
                MoviesState.ErrorLoadingMore(message)
            this is NoInternetException -> MoviesState.ErrorNoConnection(message)
            else -> MoviesState.Error(message)
        }
    }
}
