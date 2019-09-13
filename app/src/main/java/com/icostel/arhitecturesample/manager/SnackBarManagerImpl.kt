package com.icostel.arhitecturesample.manager

import android.app.Activity
import android.view.View
import javax.inject.Inject
import com.google.android.material.snackbar.Snackbar
import com.icostel.commons.utils.AppExecutors
import com.icostel.commons.utils.bind
import timber.log.Timber

class SnackBarManagerImpl
@Inject constructor(private val appExecutors: AppExecutors): SnackBarManager {

    companion object {
        private val TAG = SnackBarManagerImpl::class.java.simpleName
    }

    override fun handleMsg(activity: Activity, msg: String) {
        Timber.d("$TAG handleMsg() $msg")
        appExecutors.mainThread().execute {
            val parent = activity.bind<View>(android.R.id.content)
            Snackbar.make(parent, msg, Snackbar.LENGTH_LONG).show()
        }
    }

}