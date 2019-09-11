package com.icostel.arhitecturesample.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.icostel.arhitecturesample.navigation.AppScreenProvider
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.arhitecturesample.utils.settings.LoggedInProvider
import com.icostel.commons.navigation.ActivityNavigationAction
import javax.inject.Inject

class SplashScreenActivity : BaseActivity() {

    @Inject
    lateinit var loggedInProvider: LoggedInProvider

    @Inject
    lateinit var appScreenProvider: AppScreenProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loggedInProvider.instantValue()?.let { signedIn ->
            if (signedIn) {
                Handler().postDelayed({
                    navigateTo(ActivityNavigationAction.Builder()
                            .setScreenProvider(appScreenProvider)
                            .setScreen(AppScreenProvider.MAIN)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .build())
                }, 2000L)
            } else {
                Handler().postDelayed({
                    navigateTo(ActivityNavigationAction.Builder()
                            .setScreenProvider(appScreenProvider)
                            .setScreen(AppScreenProvider.LOGIN_USER)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .setShouldFinish(true)
                            .build())
                }, 2000L)
            }
        } ?: navigateTo(ActivityNavigationAction.Builder()
                .setScreenProvider(appScreenProvider)
                .setScreen(AppScreenProvider.LOGIN_USER)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .build())
    }
}