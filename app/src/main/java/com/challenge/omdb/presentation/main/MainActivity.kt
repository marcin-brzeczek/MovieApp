package com.challenge.omdb.presentation.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.challenge.omdb.R
import com.challenge.omdb.presentation.base.BaseActivity
import com.challenge.omdb.presentation.movies.binding.MovieBindingComponent
import com.challenge.omdb.utils.ImageLoader
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataBindingUtil.setDefaultComponent(MovieBindingComponent(imageLoader))
    }
}
