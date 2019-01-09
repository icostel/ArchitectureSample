package com.icostel.arhitecturesample.ui.splashscreen

import android.os.Bundle
import com.icostel.arhitecturesample.navigation.AppScreenProvider
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.arhitecturesample.utils.prefs.LoggedInProvider
import com.icostel.commons.navigation.ActivityNavigationAction
import javax.inject.Inject

class SplashScreenActivity : BaseActivity() {

    @Inject
    lateinit var loggedInProvider: LoggedInProvider

    @Inject
    lateinit var appScreenProvider: AppScreenProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loggedInProvider.instantValue()?.let {
            if (it) {
                navigateTo(ActivityNavigationAction.Builder()
                        .setScreenProvider(appScreenProvider)
                        .setScreen(AppScreenProvider.LIST_USERS)
                        .build())
            } else {
                navigateTo(ActivityNavigationAction.Builder()
                        .setScreenProvider(appScreenProvider)
                        .setScreen(AppScreenProvider.LOGIN_USER)
                        .setShouldFinish(true)
                        .build())
            }
        } ?: navigateTo(ActivityNavigationAction.Builder()
                .setScreenProvider(appScreenProvider)
                .setScreen(AppScreenProvider.LOGIN_USER)
                .build())
    }
}