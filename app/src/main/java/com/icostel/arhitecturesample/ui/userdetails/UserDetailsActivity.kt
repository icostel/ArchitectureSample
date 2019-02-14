package com.icostel.arhitecturesample.ui.userdetails

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.icostel.arhitecturesample.Config
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.di.ViewModelFactory
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.commons.utils.extensions.observe
import javax.inject.Inject

class UserDetailsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userDetailsViewModel: UserDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_details)
        userDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailsViewModel::class.java)
        userDetailsViewModel.navigationAction.observe(this, this@UserDetailsActivity::navigateTo)
        userDetailsViewModel.navigateToFirstFragment(intent.getStringExtra(Config.Data.USER_ID))
    }

    override fun getFragmentContainer(): Int {
        return R.id.container_details
    }
}