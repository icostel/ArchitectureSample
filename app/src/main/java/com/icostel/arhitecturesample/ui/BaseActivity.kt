package com.icostel.arhitecturesample.ui

import android.content.Context
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.di.InjectableActivity
import com.icostel.arhitecturesample.navigation.NavigationAction
import com.icostel.arhitecturesample.navigation.Navigator
import com.icostel.arhitecturesample.utils.error.ErrorData
import com.icostel.arhitecturesample.utils.error.ErrorFragment
import com.icostel.arhitecturesample.utils.error.ErrorHandler
import com.icostel.arhitecturesample.utils.error.ErrorViewModel
import com.icostel.arhitecturesample.utils.extensions.observe
import timber.log.Timber

abstract class BaseActivity : InjectableActivity(), Navigator, ErrorHandler {

    override fun navigateTo(navigationAction: NavigationAction?) {
        navigationAction?.navigate(this)
    }

    override var errorFragment: ErrorFragment? = null
        get() {
            if (field == null) {
                field = supportFragmentManager.findFragmentById(R.id.fragment_error) as ErrorFragment
                if (field != null) {
                    ViewModelProviders.of(this).get(ErrorViewModel::class.java)
                            .userAction.observe(this, this::onUserErrorAction)
                }
            }
            return field
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
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

    fun showError(message: String, shouldAutoDismiss: Boolean) {
        Timber.d("showError() error fragment null ? : %b", (errorFragment == null))
        errorFragment?.apply {
            makeError(ErrorData(message, shouldAutoDismiss))
        }
    }

    fun hideError() = errorFragment?.apply { hideError() }

    // overwrite this if you need to navigate using fragments, use the container ID
    override fun getFragmentContainer(): Int {
        return 0
    }

    override fun getContext(): Context {
        return this
    }

    override fun onUserErrorAction(errorData: ErrorData?) {
        TODO("not implemented")
    }
}
