package com.icostel.arhitecturesample.ui.screens.userdetails

import android.os.Bundle
import com.icostel.arhitecturesample.Config
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.ui.screens.BaseActivity
import com.icostel.commons.utils.extensions.observe
import javax.inject.Inject

class UserDetailsActivity : BaseActivity() {

    @Inject
    lateinit var userDetailsViewModel: UserDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_details)
        userDetailsViewModel = getViewModel()
        userDetailsViewModel.navigationAction.observe(this, this@UserDetailsActivity::navigateTo)
        userDetailsViewModel.navigateToFirstFragment(intent.getStringExtra(Config.Data.USER_ID))
    }

    override fun getFragmentContainer(): Int {
        return R.id.container_details
    }
}