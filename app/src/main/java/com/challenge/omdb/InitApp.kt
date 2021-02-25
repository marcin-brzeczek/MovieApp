package com.challenge.omdb

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import com.challenge.omdb.di.DaggerAppComponent
import timber.log.Timber

class InitApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this).build()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
