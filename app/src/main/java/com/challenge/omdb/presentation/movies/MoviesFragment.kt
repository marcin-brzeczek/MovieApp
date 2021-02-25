package com.challenge.omdb.presentation.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.challenge.omdb.R
import com.challenge.omdb.databinding.FragmentMoviesBinding
import com.challenge.omdb.presentation.base.BaseFragment
import com.challenge.omdb.presentation.base.EventObserver
import com.challenge.omdb.presentation.views.progressbar.ProgressState
import com.challenge.omdb.presentation.views.progressbar.VectorProgress
import com.challenge.omdb.utils.SchedulerProvider
import com.challenge.omdb.utils.getViewModel
import com.challenge.omdb.utils.hideKeyboard
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val SEARCHING_DELAY = 300L

class MoviesFragment : BaseFragment() {

    @Inject
    lateinit var scheduler: SchedulerProvider

    @Inject
    lateinit var disposable: CompositeDisposable

    lateinit var binding: FragmentMoviesBinding

    private lateinit var searchView: SearchView

    private var loader by VectorProgress(this)

    private val viewModel by lazy { getViewModel<MoviesViewModel>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMoviesBinding.inflate(inflater, container, false).apply {
        binding = this
        viewModel = this@MoviesFragment.viewModel
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.onButtonReloadClicked = { viewModel.reload() }
        setStatusBarColor(R.color.white, setDarkIcons = true)
        setHasOptionsMenu(true)
        setupToolbar()
        registerObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_movies_menu, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setupSearchView()
    }

    override fun onResume() {
        super.onResume()
        binding.moviesRecycler.addOnScrollListener(onScrollListener)
    }

    override fun onPause() {
        super.onPause()
        binding.moviesRecycler.removeOnScrollListener(onScrollListener)
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    private fun setupToolbar() {
        val appCompatActivity = activity as? AppCompatActivity
        appCompatActivity?.setSupportActionBar(binding.moviesToolbar)
    }

    private fun registerObservers() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            state.showLoader?.let {
                loader = if (it) {
                    ProgressState.SHOW
                } else {
                    ProgressState.HIDE
                }
            }
        })
        viewModel.eventOnClickMovie.observe(viewLifecycleOwner, EventObserver { movieId ->
            navController.navigate(
                MoviesFragmentDirections.actionMovieDetails(movieId)
            )
        })
    }

    private fun SearchView.setupSearchView() {
        setOnCloseListener {
            viewModel.setInitialState()
            false
        }
        // Recover search query after returning to the fragment
        if (viewModel.searchQuery.isNotEmpty()) {
            setQuery(viewModel.searchQuery, false)
            requestFocus()
            isIconified = false
        }
        queryHint = getString(R.string.movies_search_hint)
        observeSearchQuery()
    }

    private fun SearchView.observeSearchQuery() {
        disposable.add(
            observe
                .debounce(SEARCHING_DELAY, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .subscribeOn(scheduler.io())
                .subscribeOn(scheduler.ui())
                .subscribe { viewModel.search(it) }
        )
    }

    private val SearchView.observe: PublishSubject<String>
        get() {
            val subject = PublishSubject.create<String>()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    subject.onComplete()
                    return true
                }

                override fun onQueryTextChange(text: String): Boolean {
                    subject.onNext(text)
                    return true
                }
            })
            return subject
        }

    private val onScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard()
                }
            }
        }
}
