package com.challenge.omdb.di.module

import androidx.paging.PagedList
import com.challenge.omdb.data.model.Movie
import com.challenge.omdb.utils.SchedulerProvider
import com.challenge.omdb.utils.SchedulerProviderImpl
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = SchedulerProviderImpl()

    @Provides
    @Singleton
    fun providePagedListConfig(): PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(10)
        .setPageSize(10).build()

    @Provides
    @Singleton
    fun provideMovieDiff() = Movie.DIFF_CALLBACK

    @Provides
    fun compositeDispsable() = CompositeDisposable()
}
