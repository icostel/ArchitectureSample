package com.icostel.arhitecturesample.ui.about

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.icostel.arhitecturesample.BuildConfig
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.api.session.SessionStore
import com.icostel.arhitecturesample.manager.SnackBarManager
import com.icostel.arhitecturesample.navigation.AppScreenProvider
import com.icostel.arhitecturesample.ui.BaseFragment
import com.icostel.commons.navigation.ActivityNavigationAction
import com.icostel.commons.navigation.Navigator
import com.icostel.commons.utils.bind
import javax.inject.Inject

class AboutFragment : BaseFragment() {

    @Inject
    lateinit var appScreenProvider: AppScreenProvider

    @Inject
    lateinit var snackBarManager: SnackBarManager

    @Inject
    lateinit var sessionStore: SessionStore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val layout: View = inflater.inflate(R.layout.layout_about, container, false)

        val signOutBtn: Button = layout.bind(R.id.sign_out_btn)
        val aboutTv: TextView = layout.bind(R.id.about)
        aboutTv.text = context?.getString(R.string.about, BuildConfig.VERSION_NAME)

        signOutBtn.setOnClickListener {
            sessionStore.updateValue(null)
            snackBarManager.handleMsg(activity!!, getString(R.string.logging_out))
            Handler().postDelayed({
                (activity as Navigator).navigateTo(ActivityNavigationAction.Builder()
                        .setScreenProvider(appScreenProvider)
                        .setScreen(AppScreenProvider.LOGIN_USER)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .setShouldFinish(true)
                        .build())
            }, 2000L)
        }
        return layout
    }
}