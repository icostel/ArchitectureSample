package com.icostel.arhitecturesample.ui.newuser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.di.ViewModelFactory
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.commons.utils.AfterTextChangeListener
import com.icostel.arhitecturesample.utils.error.ErrorData
import com.icostel.arhitecturesample.utils.error.ErrorType
import com.icostel.commons.utils.extensions.observe
import com.icostel.arhitecturesample.view.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.layout_new_user.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class NewUserActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var newUserViewModel: NewUserViewModel

    private val user: User = User()

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
                        user.resourceUrl = selectedImageUri.path ?: ""
                        user_image.setImageURI(selectedImageUri)
                    }
                }
            }
        }
    }

    private fun buildUi() {
        setContentView(R.layout.layout_new_user)

        newUserViewModel.allDataAvailable.observe(this) { create_user_btn.isEnabled = it ?: false }
        create_user_btn.setOnClickListener { newUserViewModel.onAddUser(user) }
        add_user_image_fab.setOnClickListener { newUserViewModel.onAddUserImage() }
        first_name_tv.addTextChangedListener( (AfterTextChangeListener { user.firstName = it; newUserViewModel.allDataAvailable(user) }))
        last_name_tv.addTextChangedListener( (AfterTextChangeListener { user.lastName = it; newUserViewModel.allDataAvailable(user) }))
        country_tv.addTextChangedListener( (AfterTextChangeListener { user.country = it; newUserViewModel.allDataAvailable(user) }))
        age_tv.addTextChangedListener( (AfterTextChangeListener { user.age = it; newUserViewModel.allDataAvailable(user) }))

        enableUpNavigation()
    }

    private fun handleAddUserResponse(status: Boolean?) {
        status?.let {
            if (status) {
                setResult(Activity.RESULT_OK)
                showError(ErrorData("success", getString(R.string.add_user_success), "", false, null, ErrorType.Success))
                AndroidSchedulers.mainThread().createWorker().schedule({ finish() }, FINISH_DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
            } else {
                showError(ErrorData("error", getString(R.string.error_adding_user), "", false, null, ErrorType.Error))
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

    companion object {
        const val TAG = "NewUserActivity"
        const val RESULT_CODE_USER_ADDED = 6
        const val FINISH_DELAY_IN_MILLIS: Long = 2000
    }
}