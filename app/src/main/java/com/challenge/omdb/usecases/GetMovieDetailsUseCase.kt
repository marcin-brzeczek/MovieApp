package com.challenge.omdb.usecases

import com.challenge.omdb.data.api.exceptions.mapToCompliantException
import com.challenge.omdb.data.model.Movie
import com.challenge.omdb.data.repository.MoviesRepository
import com.challenge.omdb.utils.SchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val scheduler: SchedulerProvider
) : (String) -> Single<Movie> {

    override fun invoke(id: String) =
        repository
            .getMovieDetails(id)
            .onErrorResumeNext { Single.error(it.mapToCompliantException()) }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())

}
