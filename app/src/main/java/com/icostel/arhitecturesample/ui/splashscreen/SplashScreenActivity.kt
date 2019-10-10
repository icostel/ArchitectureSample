package com.icostel.arhitecturesample.ui.splashscreen

import android.os.Bundle
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.commons.utils.extensions.observe

class SplashScreenActivity : BaseActivity() {

    private lateinit var splashScreenViewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenViewModel = getViewModel()
        splashScreenViewModel.navigationAction.observe(this) { navigateTo(it) }
        splashScreenViewModel.navigate()
    }
}