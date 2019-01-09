package com.icostel.arhitecturesample.ui.splashscreen

import android.os.Bundle
import com.icostel.arhitecturesample.navigation.AppScreenProvider
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.commons.navigation.ActivityNavigationAction
import javax.inject.Inject

class SplashScreenActivity : BaseActivity() {

    @Inject
    lateinit var appScreenProvider: AppScreenProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO check if signed in and move to user list
        navigateTo(ActivityNavigationAction.Builder()
                .setScreenProvider(appScreenProvider)
                .setScreen(AppScreenProvider.MAIN)
                .build())
    }
}