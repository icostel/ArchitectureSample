package com.icostel.arhitecturesample.ui.loginuser

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.api.Status
import com.icostel.arhitecturesample.manager.SnackBarManager
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.arhitecturesample.utils.error.ErrorData
import com.icostel.arhitecturesample.utils.error.ErrorHandler
import com.icostel.commons.utils.AfterTextChangeListener
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

    private var loginBtn: Button? = null
    private var userEmailTv: TextView? = null
    private var userPassTv: TextView? = null
    private var loadingDialog: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginUserViewModel = getViewModel(LoginUserViewModel::class.java)

        setContentView(R.layout.activity_login_user)

        loginBtn = findViewById(R.id.login_btn)
        userEmailTv = findViewById(R.id.user_email_tv)
        userPassTv = findViewById(R.id.user_pass_tv)
        loadingDialog = findViewById(R.id.loading_dialog)

        userEmailTv?.addTextChangedListener((AfterTextChangeListener { loginUserViewModel.allInputsAvailable(userEmailTv?.text, userPassTv?.text) }))
        userPassTv?.addTextChangedListener((AfterTextChangeListener { loginUserViewModel.allInputsAvailable(userEmailTv?.text, userPassTv?.text) }))

        loginUserViewModel.navigationAction.observe(this) { navigateTo(it) }
        loginUserViewModel.signInStatusLive.observe(this) { handleLoginStatus(it) }
        loginUserViewModel.allInputsAvailable.observe(this) { it?.let { enabled -> loginBtn?.isEnabled = enabled } }
        loginBtn?.setOnClickListener { loginUserViewModel.onLogInBtnClicked(userEmailTv?.text.toString(), userPassTv?.text.toString()) }
    }

    private fun handleLoginStatus(type: Status.Type?) {
        type?.let {
            when (type) {
                Status.Type.IN_PROGRESS -> {
                    loadingDialog?.visibility = View.VISIBLE
                    loginBtn?.isEnabled = false
                }
                Status.Type.INPUTS_ERROR -> {
                    snackBarManager.handleMsg(this@LoginUserActivity, getString(R.string.inputs_invalid))
                    loadingDialog?.visibility = View.GONE
                    loginBtn?.isEnabled = true
                }
                Status.Type.CALL_ERROR -> {
                    snackBarManager.handleMsg(this@LoginUserActivity, getString(R.string.login_error))
                    loadingDialog?.visibility = View.GONE
                    loginBtn?.isEnabled = true
                }
                Status.Type.SUCCESS -> {
                    snackBarManager.handleMsg(this@LoginUserActivity, getString(R.string.login_success))
                    loadingDialog?.visibility = View.GONE
                    loginUserViewModel.onLoginSuccess()
                    loginBtn?.isEnabled = true
                }
                else -> {
                    Timber.d("$TAG unknown status type $type")
                }
            }
        }
    }

    override fun onUserErrorAction(errorData: ErrorData?) {
        //TODO handle error dialogs user input if any
    }
}
