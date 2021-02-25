package com.challenge.omdb.utils

import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.challenge.omdb.presentation.base.BaseActivity

inline fun <reified T : ViewModel> Fragment.getViewModel(): T {
    val viewModelFactory = (requireActivity() as BaseActivity).viewModelFactory
    return ViewModelProviders.of(this, viewModelFactory)[T::class.java]
}

fun Fragment.hideKeyboard() {
    val inputManager = this.context?.getSystemService<InputMethodManager>()
    view?.windowToken?.run {
        inputManager?.hideSoftInputFromWindow(this, 0)
    }
}
