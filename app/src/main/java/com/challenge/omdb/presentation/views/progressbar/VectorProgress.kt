package com.challenge.omdb.presentation.views.progressbar

import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.ViewOverlay
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.challenge.omdb.R
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class VectorProgress constructor(
    private val fragment: Fragment
) : ReadWriteProperty<Fragment, ProgressState> {

    private val handler = Handler()
    private var state = ProgressState.HIDE
    private var isRegistered = false

    private var groupOverlay: ViewOverlay? = null
    private var loader: AnimatedVectorDrawableCompat? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>) = state

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: ProgressState) {
        registerOnce()
        when (value) {
            ProgressState.SHOW -> showProgress()
            ProgressState.HIDE -> hideProgress()
        }
    }

    private fun registerOnce() =
        when (isRegistered) {
            false -> {
                fragment.viewLifecycleOwner.lifecycle.addObserver(viewLifecycleObserver)
                isRegistered = true
            }
            else -> { /* no op */
            }
        }

    private fun createLoader() =
        AnimatedVectorDrawableCompat
            .create(fragment.requireContext(), R.drawable.progress)
            ?.apply {
                setBounds(
                    calculateLeft().toInt(),
                    calculateTop().toInt(),
                    calculateRight().toInt(),
                    calculateBottom().toInt()
                )
                registerAnimationCallback(infiniteLooper)
            }

    private fun showProgress() = loader?.run {
        groupOverlay?.add(this)
        start()
    }

    private fun hideProgress() = loader?.run {
        stop()
        unregisterAnimationCallback(infiniteLooper)
        groupOverlay?.remove(this)
    }

    private fun calculateLeft() = (getMaxWidth() - fragment.resources.getDimension(R.dimen.size_progress)) / 2

    private fun calculateTop() = fragment.resources.getDimension(R.dimen.size_progress)

    private fun calculateRight() = calculateLeft() + fragment.resources.getDimension(R.dimen.size_progress)

    private fun calculateBottom() = calculateTop() * 2

    private fun getMaxWidth() = fragment.view?.rootView?.width ?: 0

    private val viewLifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun inflateProgress() {
            groupOverlay = fragment.view?.rootView?.overlay
            loader = createLoader()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun cleanup() {
            handler.removeCallbacksAndMessages(null)
            loader?.run {
                groupOverlay?.remove(this)
            }
        }
    }

    private val infiniteLooper = object : Animatable2Compat.AnimationCallback() {

        override fun onAnimationEnd(drawable: Drawable?) {
            fragment.view?.rootView?.post { loader?.start() }
        }
    }
}
