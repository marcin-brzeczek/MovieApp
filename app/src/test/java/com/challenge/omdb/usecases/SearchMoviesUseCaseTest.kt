package com.challenge.omdb.usecases

import com.challenge.omdb.ErrorResponseMock
import com.challenge.omdb.TestSchedulerProvider
import com.challenge.omdb.data.api.SearchMovieResponse
import com.challenge.omdb.data.api.exceptions.NoInternetException
import com.challenge.omdb.data.api.exceptions.OmdbApiException
import com.challenge.omdb.data.api.exceptions.UnauthorizeException
import com.challenge.omdb.data.model.Movie
import com.challenge.omdb.data.repository.MoviesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.reactivex.Single
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.UnknownHostException

internal class SearchMoviesUseCaseTest {

    private lateinit var useCase: SearchMoviesUseCase
    private val repository = mockk<MoviesRepository>()
    private val scheduler = TestSchedulerProvider()

    @BeforeEach
    fun before() {
        useCase = SearchMoviesUseCase(
            repository = repository,
            scheduler = scheduler
        )
    }

    @AfterEach
    fun afterEach() {
        unmockkAll()
    }

    @Test
    fun `given search query and first page when invoke return first page of movies`() {
        every { repository.searchMovies(TEST_MOVIE, 1) } returns Single.just(
            SearchMovieResponse(
                movies = movies,
                errorMessage = null
            )
        )

        useCase(TEST_MOVIE, 1)
            .test()
            .assertNoErrors()
            .assertResult(movies)
    }

    @Test
    fun `given search query and next page when invoke return next page of movies`() {
        every { repository.searchMovies(TEST_MOVIE, 2) } returns Single.just(
            SearchMovieResponse(
                movies = movies.subList(0, 1),
                errorMessage = null
            )
        )

        useCase(TEST_MOVIE, 2)
            .test()
            .assertNoErrors()
            .assertResult(movies.subList(0, 1))
    }

    @Test
    fun `given search query when no movie result return omdb api exception`() {
        val searchQuery = "A"
        val expectedErrorMessage = "Too many result."
        every { repository.searchMovies(searchQuery, 1) } returns Single.just(
            SearchMovieResponse(
                movies = null,
                errorMessage = expectedErrorMessage
            )
        )

        useCase(searchQuery, 1)
            .test()
            .assertError { error ->
                error is OmdbApiException && error.message == expectedErrorMessage
            }
    }

    @Test
    fun `given search query when HTTP 401 occured then return unauthorized exception`() {
        val expectedErrorMessage = "Unauthorized.\nCheck your API KEY!"
        every { repository.searchMovies(TEST_MOVIE, 1) } returns Single.error(
            ErrorResponseMock.unauthorize()
        )

        useCase(TEST_MOVIE, 1)
            .test()
            .assertError { error ->
                error is UnauthorizeException && error.message == expectedErrorMessage
            }
    }

    @Test
    fun `given search query when UnknownHostException occured then return no internet exception`() {
        val expectedErrorMessage = "Check your network connection!"
        every {
            repository.searchMovies(
                TEST_MOVIE,
                1
            )
        } returns Single.error(UnknownHostException())

        useCase(TEST_MOVIE, 1)
            .test()
            .assertError { error ->
                error is NoInternetException && error.message == expectedErrorMessage
            }
    }

    private companion object {
        const val TEST_MOVIE = "Marvel"
        val movies = listOf(
            Movie(TEST_MOVIE, "2000", "12345", "movie", "imageUrl1"),
            Movie(TEST_MOVIE, "2010", "12346", "movie", "imageUrl2"),
            Movie(TEST_MOVIE, "2020", "12347", "movie", "imageUrl3")
        )
    }
}
