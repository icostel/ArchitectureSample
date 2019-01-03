package com.icostel.arhitecturesample.ui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.icostel.arhitecturesample.R
import com.icostel.commons.connection.ConnectionLiveData
import com.icostel.arhitecturesample.utils.error.ErrorData
import com.icostel.arhitecturesample.utils.error.ErrorFragment
import com.icostel.arhitecturesample.utils.error.ErrorHandler
import com.icostel.arhitecturesample.utils.error.ErrorViewModel
import com.icostel.arhitecturesample.utils.extensions.observe
import com.icostel.commons.navigation.NavigationAction
import com.icostel.commons.navigation.Navigator
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector, Navigator {

    @Inject
    internal lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    internal lateinit var connectionLiveData: ConnectionLiveData

    private var errorFragment: ErrorFragment? = null

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = dispatchingAndroidInjector

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

    // overwrite this if you need to navigate using fragments, use the container ID
    override fun getFragmentContainer(): Int {
        return 0
    }

    override fun getContext(): Context {
        return this
    }

    companion object {
        private const val TAG_NO_CONNECTION = "tag_no_connection"
    }
}
