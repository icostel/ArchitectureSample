package com.icostel.arhitecturesample.ui.screens.loginuser

import android.os.Bundle
import android.widget.*
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.api.Status
import com.icostel.arhitecturesample.manager.SnackBarManager
import com.icostel.arhitecturesample.ui.screens.BaseActivity
import com.icostel.arhitecturesample.utils.error.ErrorData
import com.icostel.arhitecturesample.utils.error.ErrorHandler
import com.icostel.commons.utils.AfterTextChangeListener
import com.icostel.commons.utils.bind
import com.icostel.commons.utils.extensions.observe
import timber.log.Timber
import javax.inject.Inject

class LoginUserActivity : BaseActivity(), ErrorHandler {

    companion object {
        private val TAG = LoginUserActivity::class.java.simpleName
    }

    @Inject
    lateinit var snackBarManager: SnackBarManager

    private lateinit var loginUserViewModel: LoginUserViewModel

    private lateinit var loginBtn: Button
    private lateinit var userEmailTv: TextView
    private lateinit var userPassTv: TextView
    private lateinit var loginCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginUserViewModel = getViewModel()

        setContentView(R.layout.activity_login_user)

        loginBtn = bind(R.id.login_btn)
        userEmailTv = bind(R.id.user_email_tv)
        userPassTv = bind(R.id.user_pass_tv)
        loginCheckBox = bind(R.id.login_checkbox)

        userEmailTv.addTextChangedListener((AfterTextChangeListener {
            loginUserViewModel.allInputsAvailable(userEmailTv.text, userPassTv.text)
        }))
        userPassTv.addTextChangedListener((AfterTextChangeListener {
            loginUserViewModel.allInputsAvailable(userEmailTv.text, userPassTv.text)
        }))

        loginUserViewModel.navigationAction.observe(this) { navigateTo(it) }
        loginUserViewModel.signInStatusLive.observe(this) { handleLoginStatus(it) }
        loginUserViewModel.allInputsAvailable.observe(this) { it?.let { enabled -> loginBtn.isEnabled = enabled } }
        loginBtn.setOnClickListener { loginUserViewModel.onLogInBtnClicked(userEmailTv.text.toString(), userPassTv.text.toString()) }
        loginCheckBox.isChecked = loginUserViewModel.isKeepLogin()
        loginCheckBox.setOnCheckedChangeListener { _, checked -> loginUserViewModel.onKeepLoginSwitch(checked) }
    }

    private fun handleLoginStatus(type: Status.Type?) {
        type?.let {
            when (type) {
                Status.Type.LOADING -> {
                    setLoading(true)
                    loginBtn.isEnabled = false
                }
                Status.Type.INPUTS_ERROR -> {
                    snackBarManager.handleMsg(this@LoginUserActivity, getString(R.string.inputs_invalid))
                    setLoading(false)
                    loginBtn.isEnabled = true
                }
                Status.Type.CALL_ERROR -> {
                    snackBarManager.handleMsg(this@LoginUserActivity, getString(R.string.login_error))
                    setLoading(false)
                    loginBtn.isEnabled = true
                }
                Status.Type.SUCCESS -> {
                    snackBarManager.handleMsg(this@LoginUserActivity, getString(R.string.login_success))
                    setLoading(false)
                    loginUserViewModel.onLoginSuccess()
                    loginBtn.isEnabled = true
                }
                else -> {
                    setLoading(false)
                    Timber.d("$TAG unknown status type $type")
                }
            }
        }
    }

    override fun onUserErrorAction(errorData: ErrorData?) {
        //TODO handle error dialogs user input if any
    }
}
