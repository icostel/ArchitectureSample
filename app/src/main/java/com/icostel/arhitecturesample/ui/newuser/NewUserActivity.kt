package com.icostel.arhitecturesample.ui.newuser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.di.ViewModelFactory
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.arhitecturesample.utils.extensions.observe
import kotlinx.android.synthetic.main.layout_new_user.*
import timber.log.Timber
import javax.inject.Inject


class NewUserActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var newUserViewModel: NewUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate():: %s", TAG)

        newUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(NewUserViewModel::class.java)
        newUserViewModel.navigationAction.observe(this, this::navigateTo)

        buildUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NewUserViewModel.Const.IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.apply {
                    val selectedImageUri = data.data
                    selectedImageUri?.apply {
                        user_image.setImageURI(selectedImageUri)
                    }
                }
            }
        }
    }

    private fun buildUi() {
        setContentView(R.layout.layout_new_user)
        crate_user_btn.setOnClickListener { newUserViewModel.onAddUser() }
        add_user_image.setOnClickListener { newUserViewModel.onAddUserImage() }
        enableUpNavigation()
    }

    companion object {
        const val TAG = "NewUserActivity"
    }
}