package com.icostel.arhitecturesample.ui.screens.newuser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.manager.SnackBarManager
import com.icostel.arhitecturesample.ui.screens.BaseActivity
import com.icostel.arhitecturesample.ui.model.User
import com.icostel.commons.utils.AfterTextChangeListener
import com.icostel.commons.utils.IntentUtils
import com.icostel.commons.utils.extensions.observe
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.layout_new_user.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewUserActivity : BaseActivity() {

    @Inject
    lateinit var snackBarManager: SnackBarManager

    private lateinit var newUserViewModel: NewUserViewModel

    private val user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newUserViewModel = getViewModel()
        newUserViewModel.navigationAction.observe(this, this::navigateTo)
        newUserViewModel.apiResponse.observe(this, this::handleAddUserResponse)
        newUserViewModel.inputValidation.observe(this, this::handleUserInputError)

        buildUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IntentUtils.IMAGE_REQUEST_CODE) {
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
        first_name_tv.addTextChangedListener(AfterTextChangeListener { user.firstName = it; newUserViewModel.allDataAvailable(user) })
        last_name_tv.addTextChangedListener(AfterTextChangeListener { user.lastName = it; newUserViewModel.allDataAvailable(user) })
        country_tv.addTextChangedListener(AfterTextChangeListener { user.country = it; newUserViewModel.allDataAvailable(user) })
        age_tv.addTextChangedListener(AfterTextChangeListener { user.age = it; newUserViewModel.allDataAvailable(user) })

        enableUpNavigation()
    }

    private fun handleAddUserResponse(status: Boolean?) {
        status?.let {
            if (status) {
                setResult(Activity.RESULT_OK)
                snackBarManager.handleMsg(this@NewUserActivity,
                        getString(R.string.add_user_success))
                AndroidSchedulers.mainThread().createWorker().schedule({ finish() },
                        FINISH_DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
            } else {
                snackBarManager.handleMsg(this@NewUserActivity, getString(R.string.error_adding_user))
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
        const val REQUEST_CODE_USER_ADDED = 6
        const val FINISH_DELAY_IN_MILLIS: Long = 2000
    }
}