package com.challenge.omdb.presentation.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.android.support.DaggerFragment
import com.challenge.omdb.R

abstract class BaseFragment : DaggerFragment() {

    protected lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    protected fun doOnBackPress(removeCallbackAfterClick: Boolean = true, block: () -> Unit) {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                block()
                if (removeCallbackAfterClick) this.remove()
            }
        })
    }

    protected fun setStatusBarColor(
        colorResource: Int = R.color.colorPrimary,
        setDarkIcons: Boolean = false
    ) {
        activity?.window?.apply {
            decorView.systemUiVisibility =
                decorView.systemUiVisibility.xor(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            if (setDarkIcons) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
            statusBarColor = ContextCompat.getColor(context, colorResource)
        }
    }
}
