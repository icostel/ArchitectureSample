package com.icostel.arhitecturesample.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.icostel.arhitecturesample.api.session.SessionStore
import com.icostel.arhitecturesample.navigation.AppScreenProvider
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.commons.navigation.ActivityNavigationAction
import javax.inject.Inject

//TODO maybe we should switch to provides data in the constructor
class SplashScreenActivity : BaseActivity() {

    @Inject
    lateinit var sessionStore: SessionStore

    @Inject
    lateinit var appScreenProvider: AppScreenProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionStore.getUserToken()?.let {
            Handler().postDelayed({
                navigateTo(ActivityNavigationAction.Builder()
                        .setScreenProvider(appScreenProvider)
                        .setScreen(AppScreenProvider.MAIN)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .build())
            }, 2000L)
        } ?: run {
            Handler().postDelayed({
                navigateTo(ActivityNavigationAction.Builder()
                        .setScreenProvider(appScreenProvider)
                        .setScreen(AppScreenProvider.LOGIN_USER)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .build())
            }, 2000L)
        }
    }
}