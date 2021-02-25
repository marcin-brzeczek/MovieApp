package com.challenge.omdb.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.challenge.omdb.presentation.details.MovieDetailsFragment
import com.challenge.omdb.presentation.movies.MoviesFragment

@Module
abstract class FragmentInjectorsModule {

    @ContributesAndroidInjector
    abstract fun provideCurrentListFragment(): MoviesFragment

    @ContributesAndroidInjector
    abstract fun provideAddShoppingItem(): MovieDetailsFragment
}
