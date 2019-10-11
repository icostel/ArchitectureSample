package com.icostel.arhitecturesample.ui.screens.loginuser

import android.text.TextUtils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.icostel.arhitecturesample.api.Status
import com.icostel.arhitecturesample.api.session.SessionStore
import com.icostel.arhitecturesample.navigation.AppScreenProvider
import com.icostel.arhitecturesample.repository.UserLogInHandler
import com.icostel.commons.navigation.ActivityNavigationAction
import com.icostel.commons.navigation.NavigationAction
import com.icostel.commons.utils.livedata.SingleLiveEvent

import javax.inject.Inject

class LoginUserViewModel
@Inject internal constructor(
        private val sessionStore: SessionStore,
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
            signInStatusLive.value = Status.Type.INPUTS_ERROR
        } else {
            signInStatusLive.value = Status.Type.LOADING
            userLogInHandler.signInUser(userEmail, userPass,
                    object : UserLogInHandler.OnUserSignInResultListener {
                        override fun onUserSignInResult(status: Status) {
                            if (status.type == Status.Type.SUCCESS) {
                                sessionStore.setUserCredentials(userEmail, userPass)
                            }
                            signInStatusLive.postValue(status.type)
                        }
                    })
        }
    }

    internal fun onKeepLoginSwitch(keep: Boolean) {
        sessionStore.setKeepLogin(keep)
    }

    internal fun isKeepLogin(): Boolean {
        return sessionStore.getKeepLogin() ?: false
    }

    internal fun onLoginSuccess() {
        navigationAction.postValue(
                ActivityNavigationAction.Builder()
                        .setScreenProvider(appScreenProvider)
                        .setScreen(AppScreenProvider.MAIN)
                        .setShouldFinish(true)
                        .build()
        )
    }
}
