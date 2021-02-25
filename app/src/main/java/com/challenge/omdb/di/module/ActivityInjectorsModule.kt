package com.challenge.omdb.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.challenge.omdb.presentation.main.MainActivity

@Module
abstract class ActivityInjectorsModule {
    @ContributesAndroidInjector
    abstract fun provideMainActivityInjector(): MainActivity
}
