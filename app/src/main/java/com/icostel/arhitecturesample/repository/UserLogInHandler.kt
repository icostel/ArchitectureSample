package com.icostel.arhitecturesample.repository

import com.icostel.arhitecturesample.api.SignInStatus
import com.icostel.arhitecturesample.api.UserApiService
import com.icostel.arhitecturesample.api.model.SignInResponse
import com.icostel.arhitecturesample.api.session.SessionStore
import com.icostel.arhitecturesample.api.utils.ApiErrorResponse
import com.icostel.arhitecturesample.api.utils.ApiResponse
import com.icostel.arhitecturesample.api.utils.ApiSuccessResponse
import com.icostel.arhitecturesample.utils.AppExecutors
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLogInHandler @Inject
internal constructor(private val appExecutors: AppExecutors, private val userApiService: UserApiService, private val sessionStore: SessionStore) {

    // other API calls will use the token obtained from user sign in and stored in session store
    fun signInUser(userEmail: String, userPass: String,
                   listener: OnUserSignInResultListener) {
        // add a delay to simulate a bad network or something
        Schedulers.io().createWorker().schedule({
            appExecutors.networkIO().execute {
                try {
                    manageResponse(ApiResponse.create(userApiService.signInUser(userEmail, userPass).execute()), listener)
                } catch (e: Exception) {
                    Timber.e(e)
                    listener.onUserSignInResult(SignInStatus.Error())
                }
            }
        }, 3000, TimeUnit.MILLISECONDS)
    }

    private fun manageResponse(response: ApiResponse<SignInResponse>, listener: OnUserSignInResultListener) {
        when (response) {
            is ApiSuccessResponse<*> -> {
                val (success, _, token) = (response as ApiSuccessResponse<SignInResponse>).body
                if (success) {
                    sessionStore.userSessionToken = token
                    listener.onUserSignInResult(SignInStatus.Success())
                    Timber.d("===Sign in success: " + (response as ApiSuccessResponse<*>).body)
                } else {
                    listener.onUserSignInResult(SignInStatus.Error())
                    Timber.d("===Sign in error: " + (response as ApiErrorResponse<*>).errorMessage)
                }
            }
            is ApiErrorResponse<*> -> {
                listener.onUserSignInResult(SignInStatus.Error())
                Timber.d("===Sign in error: " + (response as ApiErrorResponse<*>).errorMessage)
            }
        }
    }

    interface OnUserSignInResultListener {
        fun onUserSignInResult(status: SignInStatus)
    }
}
