package com.challenge.omdb.presentation.details

import androidx.lifecycle.Observer
import com.challenge.omdb.InstantExecutorExtension
import com.challenge.omdb.data.api.exceptions.NoInternetException
import com.challenge.omdb.data.model.Movie
import com.challenge.omdb.usecases.GetMovieDetailsUseCase
import io.mockk.*
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.UnknownHostException
import kotlin.test.assertTrue

@ExtendWith(InstantExecutorExtension::class)
internal class MovieDetailsViewModelTest {

    private lateinit var viewModel: MovieDetailsViewModel
    private var stateObserver = spyk(Observer<MovieDetailsState> {})

    private val getDetailsMovieUseCase: GetMovieDetailsUseCase = mockk()

    @BeforeEach
    fun setUp() {
        viewModel = MovieDetailsViewModel(
            application = mockk(),
            getMovieDetailsUseCase = getDetailsMovieUseCase
        ).apply {
            compositeDisposable = CompositeDisposable()
        }
        stateObserver = spyk(Observer { })
        viewModel.state.observeForever(stateObserver)
    }

    @AfterEach
    fun after() {
        viewModel.state.removeObserver(stateObserver)
    }

    @Test
    fun `given movie id when fetched successfully then returns correct state`() {
        // Given
        val state = slot<MovieDetailsState>()
        every { getDetailsMovieUseCase(MOVIE_ID) } returns Single.just(TEST_MOVIE)

        // When
        viewModel.getMovieDetails(MOVIE_ID)

        // Then
        verify { stateObserver.onChanged(capture(state)) }
        state.captured.run {
            assertTrue(this is MovieDetailsState.Success)
            assertTrue { movie == TEST_MOVIE }
        }
    }

    @Test
    fun `given movie id when UnknownHostException occured then return error state`() {
        // Given
        val state = slot<MovieDetailsState>()
        every { getDetailsMovieUseCase(MOVIE_ID) } returns Single.error(NoInternetException(UnknownHostException()))
        // When
        viewModel.getMovieDetails(MOVIE_ID)

        // Then
        verify { stateObserver.onChanged(capture(state)) }
        state.captured.run {
            assertTrue(this is MovieDetailsState.Error && this.errorMessage != null)
        }
    }

    @Test
    fun `given movie id when uknown error occured then return error state`() {
        // Given
        val state = slot<MovieDetailsState>()
        every { getDetailsMovieUseCase(MOVIE_ID) } returns Single.error(Throwable())
        // When
        viewModel.getMovieDetails(MOVIE_ID)

        // Then
        verify { stateObserver.onChanged(capture(state)) }
        state.captured.run {
            assertTrue(this is MovieDetailsState.Error && this.errorMessage == null)
        }
    }
    

    private companion object {
        const val MOVIE_ID = "12345"
        val TEST_MOVIE = Movie(
            "Marvel",
            "2000",
            "12345",
            "movie",
            "imageUrl1"
        )
    }
}
