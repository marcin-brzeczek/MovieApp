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

internal class GetMovieDetailsUseCaseTest {

    private lateinit var useCase: GetMovieDetailsUseCase
    private val repository = mockk<MoviesRepository>()
    private val scheduler = TestSchedulerProvider()

    @BeforeEach
    fun before() {
        useCase = GetMovieDetailsUseCase(
            repository = repository,
            scheduler = scheduler
        )
    }

    @AfterEach
    fun afterEach() {
        unmockkAll()
    }

    @Test
    fun `given movie id when invoke return movie details`() {
        every { repository.getMovieDetails(TEST_MOVIE_ID) } returns Single.just(
            TEST_MOVIE
        )

        useCase(TEST_MOVIE_ID)
            .test()
            .assertNoErrors()
            .assertResult(TEST_MOVIE)
    }

    @Test
    fun `given given movie id when HTTP 401 occured then return unauthorized exception`() {
        val expectedErrorMessage = "Unauthorized.\nCheck your API KEY!"
        every { repository.getMovieDetails(TEST_MOVIE_ID) } returns Single.error(
            ErrorResponseMock.unauthorize()
        )

        useCase(TEST_MOVIE_ID)
            .test()
            .assertError { error ->
                error is UnauthorizeException && error.message == expectedErrorMessage
            }
    }

    @Test
    fun `given movie id when UnknownHostException occured then return no internet exception`() {
        val expectedErrorMessage = "Check your network connection!"
        every {
            repository.getMovieDetails(
                TEST_MOVIE_ID
            )
        } returns Single.error(UnknownHostException())

        useCase(TEST_MOVIE_ID)
            .test()
            .assertError { error ->
                error is NoInternetException && error.message == expectedErrorMessage
            }
    }

    private companion object {
        const val TEST_MOVIE_ID = "12345"
         val TEST_MOVIE = Movie(
            "Marvel",
            "2000",
            "12345",
            "movie",
            "imageUrl1"
        )
    }
}
