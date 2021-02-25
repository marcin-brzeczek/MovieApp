package com.challenge.omdb.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.challenge.omdb.R
import javax.inject.Inject

interface ImageLoader {
    fun loadImage(context: Context, url: String?, target: ImageView)
}

class ImageLoaderImpl @Inject constructor() :
    ImageLoader {
    override fun loadImage(context: Context, url: String?, target: ImageView) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.movie_place_holder)
            .fallback(R.drawable.progress_animation)
            .into(target)
    }
}
