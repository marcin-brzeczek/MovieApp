package com.challenge.omdb.presentation.movies.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.challenge.omdb.utils.ImageLoader
import javax.inject.Inject

class MovieBinding @Inject constructor(
    private val imageLoader: ImageLoader
) {

    @BindingAdapter("image")
    fun loadViewerImage(imageView: ImageView, url: String?) {
        imageLoader.loadImage(imageView.context, url, imageView)
    }
}
