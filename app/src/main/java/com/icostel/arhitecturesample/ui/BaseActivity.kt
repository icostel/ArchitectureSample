package com.icostel.arhitecturesample.ui

import android.content.Context
import android.view.MenuItem
import com.icostel.arhitecturesample.di.InjectableActivity
import com.icostel.arhitecturesample.navigation.NavigationAction
import com.icostel.arhitecturesample.navigation.Navigator
import timber.log.Timber

abstract class BaseActivity : InjectableActivity(), Navigator {

    override fun navigateTo(navigationAction: NavigationAction?) {
        navigationAction?.navigate(this)
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

    // overwrite this if you need to navigate using fragments, use the container ID
    override fun getFragmentContainer(): Int {
        return 0
    }

    override fun getContext(): Context {
        return this
    }
}
