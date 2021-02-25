package com.challenge.omdb.di.module

import com.challenge.omdb.utils.ImageLoader
import com.challenge.omdb.utils.ImageLoaderImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ImageInjectorsModule {

    @Binds
    abstract fun bindsImageLoader(
        imageLoaderImpl: ImageLoaderImpl
    ): ImageLoader
}
