package com.challenge.omdb.presentation.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.challenge.omdb.R
import com.challenge.omdb.databinding.FragmentMovieDetailsBinding
import com.challenge.omdb.presentation.base.BaseFragment
import com.challenge.omdb.utils.getViewModel

class MovieDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentMovieDetailsBinding

    internal val viewModel: MovieDetailsViewModel by lazy { getViewModel<MovieDetailsViewModel>() }

    private val movie: MovieDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMovieDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
        viewModel = this@MovieDetailsFragment.viewModel
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setStatusBarColor(R.color.greyPrimary)
        registerObservers()
        viewModel.getMovieDetails(movie.id)
        doOnBackPress { navController.navigateUp() }
    }

    private fun registerObservers() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            if (state is MovieDetailsState.Error) {
                showErroDialog(state.errorMessage ?: getString(R.string.movie_details_default_error))
            }
        })
    }

    private fun showErroDialog(message: String): AlertDialog = AlertDialog.Builder(requireContext())
        .setTitle(getString(R.string.movies_details_error_dialog_title))
        .setCancelable(false)
        .setMessage(message)
        .setPositiveButton(requireContext().getString(R.string.movies_details_error_dialog_confirm_button)) { dialog, _ ->
            dialog.dismiss()
            navController.navigateUp()
        }
        .show()
}
