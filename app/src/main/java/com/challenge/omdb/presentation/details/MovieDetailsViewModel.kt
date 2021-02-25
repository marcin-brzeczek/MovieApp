package com.challenge.omdb.presentation.details

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.challenge.omdb.presentation.base.BaseViewModel
import com.challenge.omdb.usecases.GetMovieDetailsUseCase
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    application: Application,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : BaseViewModel(application) {

    val state : MutableLiveData<MovieDetailsState> = MutableLiveData()

    fun getMovieDetails(movieId: String) {
        getMovieDetailsUseCase(movieId)
            .subscribeBy(
                onSuccess = { movieDetails ->
                    state.value = MovieDetailsState.Success(movieDetails)
                },
                onError = { error ->
                    state.value = MovieDetailsState.Error(error.message)
                    Timber.e(error, "Error fetching movies")
                }
            )
            .addToDisposables()
    }
}
