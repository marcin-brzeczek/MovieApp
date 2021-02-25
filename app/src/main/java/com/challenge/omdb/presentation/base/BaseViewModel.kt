package com.challenge.omdb.presentation.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseViewModel(application: Application) :  AndroidViewModel(application) {

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    protected fun Disposable.addToDisposables() {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
