package com.icostel.arhitecturesample.ui

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.di.Injectable
import timber.log.Timber
import javax.inject.Inject

open class BaseFragment : BackFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @MainThread
    internal inline fun <reified T : ViewModel?> getViewModel(viewModelClass: Class<T>): T {
        return ViewModelProviders.of(this, viewModelFactory).get(T::class.java)
    }

    private var loadingView: LoadingView? = null
    private var actionBarColorDrawable: ColorDrawable? = null
    protected open var hasLoading = false
    private val actionBarAnimationTimeMillis = android.R.integer.config_longAnimTime

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (hasLoading && loadingView == null) {
            loadingView = LoadingView.attach(this)
        }
    }

    protected open fun setLoading(isLoading: Boolean) {
        loadingView?.isLoading = isLoading
    }

    fun setActionBarBackgroundColor(@ColorRes colorResId: Int) {
        setActionBarBackgroundColor(colorResId, actionBarAnimationTimeMillis)
    }

    fun setActionBarHomeIndication(@DrawableRes res: Int) {
        if (activity != null) {
            val actionBar = (activity as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(res)
        }
    }

    fun enableUpNavigation(enable: Boolean) {
        if (activity == null) {
            Timber.w("enableUpNavigation(), not activity available")
        } else {
            val actionBar = (activity as AppCompatActivity).supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(enable)
        }
    }

    private fun showTitle(title: String?) {
        if (activity == null) {
            Timber.w("showTitle(), not activity available")
        } else {
            title?.let {
                activity!!.title = it
            }
        }
    }

    fun hideTitle() {
        showTitle("")
    }

    private fun setActionBarBackgroundColor(@ColorRes colorResId: Int, animationTime: Int = actionBarAnimationTimeMillis) {
        // animate from the current color drawable to the provided one
        if (activity != null) {
            val compatActivity = (activity as AppCompatActivity)
            val actionBar = compatActivity.supportActionBar
            if (actionBarColorDrawable == null) {
                actionBarColorDrawable = ColorDrawable(compatActivity.resources.getColor(R.color.colorPrimary))
            }
            val nextDrawable = ColorDrawable(compatActivity.resources.getColor(colorResId))
            val color = arrayOf(nextDrawable, actionBarColorDrawable)
            val trans = TransitionDrawable(color)
            actionBar?.setBackgroundDrawable(trans)
            trans.startTransition(animationTime)
            actionBarColorDrawable = nextDrawable
        }
    }
}