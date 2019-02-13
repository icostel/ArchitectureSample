package com.icostel.arhitecturesample.ui.about

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.navigation.AppScreenProvider
import com.icostel.arhitecturesample.ui.BaseFragment
import com.icostel.arhitecturesample.utils.settings.LoggedInProvider
import com.icostel.commons.navigation.ActivityNavigationAction
import com.icostel.commons.navigation.Navigator
import com.icostel.commons.utils.bind
import javax.inject.Inject

class AboutFragment : BaseFragment() {

    @Inject
    lateinit var appScreenProvider: AppScreenProvider

    @Inject
    lateinit var loggedInProvider: LoggedInProvider

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout: View = inflater.inflate(R.layout.layout_about, container, false)
        val signOutBtn: Button = layout.bind(R.id.sign_out_btn)
        signOutBtn.setOnClickListener {
            loggedInProvider.updateValue(false)
            // got to splash screen
            (activity as Navigator).navigateTo(ActivityNavigationAction.Builder()
                    .setScreenProvider(appScreenProvider)
                    .setScreen(AppScreenProvider.LOGIN_USER)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .setShouldFinish(true)
                    .build())
        }
        return layout
    }
}