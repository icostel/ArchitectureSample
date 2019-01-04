package com.icostel.arhitecturesample.ui.newuser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.di.ViewModelFactory
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.arhitecturesample.utils.error.ErrorData
import com.icostel.arhitecturesample.utils.error.ErrorType
import com.icostel.arhitecturesample.utils.extensions.observe
import com.icostel.arhitecturesample.view.model.User
import kotlinx.android.synthetic.main.layout_new_user.*
import kotlinx.android.synthetic.main.layout_new_user.view.*
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
        newUserViewModel.apiResponse.observe(this, this::handleAddUserResponse)

        buildUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NewUserViewModel.IMAGE_REQUEST_CODE) {
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
        crate_user_btn.setOnClickListener { addUser() }
        add_user_image.setOnClickListener { newUserViewModel.onAddUserImage() }
        enableUpNavigation()
    }

    private fun handleAddUserResponse(status: Boolean?) {
        status?.let {
            if (status) {
                showError(ErrorData("error", getString(R.string.add_user_success), "", false, null, ErrorType.Success))
            } else {
                showError(ErrorData("success", getString(R.string.error_adding_user), "", false, null, ErrorType.Error))
            }
        }
    }

    private fun addUser() {
        //TODO validation and ui error handling
        val user = User()
        user.firstName = first_name_tv.text.toString()
        user.lastName = last_name_tv.text.toString()
        user.country = country_tv.text.toString()
        user.age = Integer.parseInt(age_tv.text.toString())
        newUserViewModel.onAddUser(user)
    }

    companion object {
        const val TAG = "NewUserActivity"
    }
}