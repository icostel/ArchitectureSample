package com.icostel.arhitecturesample.ui.loginuser

import android.os.Handler
import android.text.TextUtils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.icostel.arhitecturesample.api.Status
import com.icostel.arhitecturesample.navigation.AppScreenProvider
import com.icostel.arhitecturesample.repository.UserLogInHandler
import com.icostel.commons.navigation.ActivityNavigationAction
import com.icostel.commons.navigation.NavigationAction
import com.icostel.commons.utils.livedata.SingleLiveEvent

import javax.inject.Inject

class LoginUserViewModel
@Inject internal constructor(
        private val userLogInHandler: UserLogInHandler,
        private val appScreenProvider: AppScreenProvider)
    : ViewModel() {

    // used in the UI for displaying the log in status
    internal val signInStatusLive: MutableLiveData<Status.Type> = MutableLiveData()
    // used for enabling/disabling the log in btn
    internal val allInputsAvailable: MutableLiveData<Boolean> = MutableLiveData()
    // used for navigation
    internal val navigationAction: SingleLiveEvent<NavigationAction> = SingleLiveEvent()

    init {
        this.allInputsAvailable.postValue(false)
        this.signInStatusLive.postValue(Status.Type.NOT_STARTED)
    }

    internal fun allInputsAvailable(userEmail: CharSequence, userPass: CharSequence) {
        allInputsAvailable.postValue(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass))
    }

    // the log in
    internal fun onLogInBtnClicked(userEmail: String, userPass: String) {
        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPass)) {
            signInStatusLive.setValue(Status.Type.INPUTS_ERROR)
        } else {
            signInStatusLive.value = Status.Type.SUCCESS
            userLogInHandler.signInUser(userEmail, userPass,
                    object : UserLogInHandler.OnUserSignInResultListener {
                        override fun onUserSignInResult(status: Status) {
                            signInStatusLive.postValue(status.type)
                        }
                    })
        }
    }

    internal fun onLoginSuccess() {
        Handler().postDelayed({
            navigationAction.postValue(
                    ActivityNavigationAction.Builder()
                            .setScreenProvider(appScreenProvider)
                            .setScreen(AppScreenProvider.MAIN)
                            .setShouldFinish(true)
                            .build()
            )
        }, 2000L)
    }
}
