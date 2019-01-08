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
import timber.log.Timber
import javax.inject.Inject


class NewUserActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var newUserViewModel: NewUserViewModel
    private var imageUri = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate():: %s", TAG)

        newUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(NewUserViewModel::class.java)
        newUserViewModel.navigationAction.observe(this, this::navigateTo)
        newUserViewModel.apiResponse.observe(this, this::handleAddUserResponse)
        newUserViewModel.inputValidation.observe(this, this::handleUserInputError)

        buildUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NewUserViewModel.IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.apply {
                    val selectedImageUri = data.data
                    selectedImageUri?.apply {
                        imageUri = selectedImageUri.toString()
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

    private fun handleUserInputError(error: Int?) {
        error?.let {
            when (error) {
                NewUserViewModel.ERROR_FIRST_NAME -> first_name_input_layout.error = getString(R.string.first_name_not_valid)
                NewUserViewModel.ERROR_LAST_NAME -> last_name_input_layout.error = getString(R.string.last_name_not_valid)
                NewUserViewModel.ERROR_COUNTRY -> country_input_layout.error = getString(R.string.country_name_not_valid)
                NewUserViewModel.ERROR_AGE -> age_input_layout.error = getString(R.string.age_not_valid)
                else -> {
                    first_name_input_layout.error = ""
                    last_name_input_layout.error = ""
                    country_input_layout.error = ""
                    age_input_layout.error = ""
                }
            }
        }
    }

    private fun addUser() {
        // the id will be set after the adding is done on the API, the BE will provide that
        newUserViewModel.onAddUser(User("",
                first_name_tv.text.toString(),
                last_name_tv.text.toString(),
                imageUri,
                country_tv.text.toString(),
                age_tv.text.toString()
        ))
    }

    companion object {
        const val TAG = "NewUserActivity"
    }
}