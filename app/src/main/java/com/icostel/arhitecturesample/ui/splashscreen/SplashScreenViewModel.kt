package com.icostel.arhitecturesample.ui.splashscreen

import android.content.Intent
import android.os.Handler
import com.icostel.arhitecturesample.api.session.SessionStore
import com.icostel.arhitecturesample.navigation.AppScreenProvider
import com.icostel.commons.BaseViewModel
import com.icostel.commons.navigation.ActivityNavigationAction
import com.icostel.commons.navigation.NavigationAction
import com.icostel.commons.utils.livedata.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

class SplashScreenViewModel
@Inject constructor(
        private var appScreenProvider: AppScreenProvider,
        private var sessionStore: SessionStore
) : BaseViewModel() {

    internal val navigationAction: SingleLiveEvent<NavigationAction> = SingleLiveEvent()

    fun navigate() {
        Timber.d("$TAG navigate() user token: ${sessionStore.getUserToken()}")

        sessionStore.getUserToken()?.let {
            Handler().postDelayed({
                navigationAction.postValue(ActivityNavigationAction.Builder()
                        .setScreenProvider(appScreenProvider)
                        .setScreen(AppScreenProvider.MAIN)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .build())
            }, NAVIGATION_DELAY)
        } ?: run {
            Handler().postDelayed({
                navigationAction.postValue(ActivityNavigationAction.Builder()
                        .setScreenProvider(appScreenProvider)
                        .setScreen(AppScreenProvider.LOGIN_USER)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .build())
            }, NAVIGATION_DELAY)
        }
    }

    companion object {
        private val TAG = SplashScreenViewModel::class.java.simpleName
        private const val NAVIGATION_DELAY = 2000L
    }
}