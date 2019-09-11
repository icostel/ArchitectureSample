package com.icostel.arhitecturesample.repository

import com.icostel.arhitecturesample.api.Status
import com.icostel.arhitecturesample.api.UserApiService
import com.icostel.arhitecturesample.api.model.SignInResponse
import com.icostel.arhitecturesample.api.session.SessionStore
import com.icostel.arhitecturesample.api.utils.ApiErrorResponse
import com.icostel.arhitecturesample.api.utils.ApiResponse
import com.icostel.arhitecturesample.api.utils.ApiSuccessResponse
import com.icostel.arhitecturesample.utils.settings.LoggedInProvider
import com.icostel.commons.utils.AppExecutors
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLogInHandler @Inject
internal constructor(private val appExecutors: AppExecutors, private val userApiService: UserApiService, private val sessionStore: SessionStore, private val loggedInProvider: LoggedInProvider) {

    // other API calls will use the token obtained from user sign in and stored in session store
    fun signInUser(userEmail: String, userPass: String,
                   listener: OnUserSignInResultListener) {
        appExecutors.delayedNetworkIO().execute {
            try {
                manageResponse(ApiResponse.create(userApiService.signInUser(userEmail, userPass).execute()), listener)
            } catch (e: Exception) {
                Timber.e(e)
                listener.onUserSignInResult(Status.Error())
            }
        }
    }

    private fun manageResponse(response: ApiResponse<SignInResponse>, listener: OnUserSignInResultListener) {
        when (response) {
            is ApiSuccessResponse<*> -> {
                val (success, _, token) = (response as ApiSuccessResponse<SignInResponse>).body
                if (success) {
                    sessionStore.userSessionToken = token
                    loggedInProvider.updateValue(true)
                    listener.onUserSignInResult(Status.Success())
                    Timber.d("===Sign in success: " + (response as ApiSuccessResponse<*>).body)
                } else {
                    listener.onUserSignInResult(Status.Error())
                    loggedInProvider.updateValue(true)
                    Timber.d("===Sign in error: " + (response as ApiErrorResponse<*>).errorMessage)
                }
            }
            is ApiErrorResponse<*> -> {
                listener.onUserSignInResult(Status.Error())
                Timber.d("===Sign in error: " + (response as ApiErrorResponse<*>).errorMessage)
            }
        }
    }

    interface OnUserSignInResultListener {
        fun onUserSignInResult(status: Status)
    }
}
