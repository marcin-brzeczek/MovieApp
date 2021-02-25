package com.challenge.omdb.presentation.movies.binding

import androidx.databinding.DataBindingComponent
import com.challenge.omdb.utils.ImageLoader

class MovieBindingComponent(
    private val imageLoader: ImageLoader
) : DataBindingComponent {

    override fun getMovieBinding(): MovieBinding {
        return MovieBinding(imageLoader)
    }
}
