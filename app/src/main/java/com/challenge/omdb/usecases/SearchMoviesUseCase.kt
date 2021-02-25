package com.challenge.omdb.usecases

import com.challenge.omdb.data.api.exceptions.OmdbApiException
import com.challenge.omdb.data.api.exceptions.mapToCompliantException
import com.challenge.omdb.data.model.Movie
import com.challenge.omdb.data.repository.MoviesRepository
import com.challenge.omdb.utils.SchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val scheduler: SchedulerProvider
) : (String, Int) -> Single<List<Movie>> {

    override fun invoke(title: String, page: Int) =
        repository
            .searchMovies(title, page)
            .map { searchMovieResponse ->
                searchMovieResponse.movies
                    ?: throw OmdbApiException(searchMovieResponse.errorMessage)
            }
            .onErrorResumeNext { Single.error(it.mapToCompliantException()) }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())

}
