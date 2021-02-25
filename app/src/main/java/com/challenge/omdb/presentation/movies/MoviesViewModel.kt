package com.challenge.omdb.presentation.movies

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import com.challenge.omdb.BR
import com.challenge.omdb.R
import com.challenge.omdb.data.model.Movie
import com.challenge.omdb.presentation.base.BaseViewModel
import com.challenge.omdb.presentation.base.Event
import com.challenge.omdb.presentation.movies.paging.MovieDataFactory
import com.challenge.omdb.utils.EMPTY_STRING
import me.tatarka.bindingcollectionadapter2.OnItemBind
import java.util.concurrent.Executors
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    app: Application,
    pagedListConfig: PagedList.Config,
    val movieDiff: DiffUtil.ItemCallback<Movie>,
    private val movieDataFactory: MovieDataFactory
) : BaseViewModel(app) {

    var searchQuery: String = EMPTY_STRING

    val state = Transformations.switchMap(movieDataFactory.movieSourceLiveData) { dataSource ->
        dataSource.moviesState
    }

    val eventOnClickMovie = MutableLiveData<Event<String>>()

    val pagedList: LiveData<PagedList<Movie>> =
        LivePagedListBuilder(movieDataFactory, pagedListConfig)
            .setFetchExecutor(Executors.newFixedThreadPool(5))
            .build()

    fun search(query: String) {
        searchQuery = query
        movieDataFactory.setQueryString(searchQuery)
        movieDataFactory.invalidate()
    }

    val itemBinding = OnItemBind<Movie> { itemBinding, _, _ ->
        itemBinding.set(BR.movie, R.layout.item_movie)
        itemBinding.bindExtra(BR.onMovieClicked, { it: Movie ->
            eventOnClickMovie.value = Event(it.id)
        })
    }

    fun setInitialState() {
        movieDataFactory.updateState(MoviesState.Initial)
    }

    fun reload() {
        search(searchQuery)
    }

    override fun onCleared() {
        movieDataFactory.clearDisposables()
        super.onCleared()
    }
}
