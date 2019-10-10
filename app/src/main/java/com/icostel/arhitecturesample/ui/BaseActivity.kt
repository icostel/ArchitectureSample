package com.icostel.arhitecturesample.ui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.utils.ConnectionLiveData
import com.icostel.arhitecturesample.utils.error.ErrorData
import com.icostel.arhitecturesample.utils.error.ErrorFragment
import com.icostel.arhitecturesample.utils.error.ErrorHandler
import com.icostel.arhitecturesample.utils.error.ErrorViewModel
import com.icostel.commons.navigation.NavigationAction
import com.icostel.commons.navigation.Navigator
import com.icostel.commons.utils.extensions.observe
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), Navigator {

    @Inject
    internal lateinit var connectionLiveData: ConnectionLiveData
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var errorFragment: ErrorFragment? = null

    // the param is used to identify the class at compile time so that
    // it's instance can pe provided from the factory (ViewModelFactoryModuleModule.kt)
    inline fun <reified T : ViewModel?> getViewModel(): T {
        return ViewModelProviders.of(this, viewModelFactory).get(T::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectionLiveData.observe(this) { onConnectionStateChanged(it) }
    }

    private fun onConnectionStateChanged(connected: Boolean?) {
        if (connected == false) {
            showError(
                    ErrorData(
                            TAG_NO_CONNECTION,
                            getString(R.string.error_no_internet_connection_io),
                            null,
                            false))
        } else {
            errorFragment?.hideError(TAG_NO_CONNECTION)
        }
    }

    override fun navigateTo(navigationAction: NavigationAction?) {
        navigationAction?.navigate(this)
    }

    fun showError(errorData: ErrorData?) {
        errorData?.let {
            ensureErrorFragment()
            errorFragment?.apply {
                makeError(errorData)
            }
        }
    }

    @MainThread
    private fun ensureErrorFragment() {
        if (errorFragment == null) {
            errorFragment = supportFragmentManager.findFragmentByTag(getString(R.string.error_fragment)) as ErrorFragment?
            if (errorFragment == null) {
                errorFragment = ErrorFragment()
                supportFragmentManager.beginTransaction().add(android.R.id.content, errorFragment!!).commit()
                supportFragmentManager.executePendingTransactions()
            }
            if (errorFragment != null && this is ErrorHandler) {
                ViewModelProviders.of(this).get(ErrorViewModel::class.java)
                        .userAction.observe(this, this::onUserErrorAction)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun enableUpNavigation() {
        val actionBar = supportActionBar
        if (actionBar == null) {
            Timber.w("no action bar available")
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    protected fun enableUpNavigation(enable: Boolean) {
        val actionBar = supportActionBar
        if (actionBar == null) {
            Timber.w("no action bar available")
        } else {
            if (enable) {
                actionBar.setDisplayHomeAsUpEnabled(true)
            } else {
                actionBar.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    protected fun hideTitle() {
        val actionBar = supportActionBar
        if (actionBar == null) {
            Timber.w("no action bar available")
        } else {
            actionBar.title = ""
        }
    }

    override fun getContext(): Context {
        return this
    }

    companion object {
        private const val TAG_NO_CONNECTION = "tag_no_connection"
    }
}
