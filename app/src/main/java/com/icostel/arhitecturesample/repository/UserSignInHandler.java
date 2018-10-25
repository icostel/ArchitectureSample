package com.icostel.arhitecturesample.repository;

import com.icostel.arhitecturesample.api.UserApiService;
import com.icostel.arhitecturesample.api.utils.ApiErrorResponse;
import com.icostel.arhitecturesample.api.utils.ApiResponse;
import com.icostel.arhitecturesample.api.utils.ApiSuccessResponse;
import com.icostel.arhitecturesample.api.utils.SignInResponse;
import com.icostel.arhitecturesample.api.utils.SignInStatus;
import com.icostel.arhitecturesample.utils.AppExecutors;
import com.icostel.arhitecturesample.api.utils.SessionStore;

import java.io.IOException;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import timber.log.Timber;

public class UserSignInHandler {

    private UserApiService userApiService;
    private AppExecutors appExecutors;
    private SessionStore sessionStore;

    @Inject
    UserSignInHandler(AppExecutors executors, UserApiService userApiService, SessionStore sessionStore) {
        this.userApiService = userApiService;
        this.appExecutors = executors;
        this.sessionStore = sessionStore;
    }

    //TODO create a sign in manager that uses this handler and has a session live data that gets updated by this call
    // and use that to trigger the sign in
    void signInUser(@NonNull String userEmail, @NonNull String userPass,
                    @NonNull OnUserSignInResultListener listener) {
        appExecutors.networkIO().execute(() -> {
            try {
                manageResponse(ApiResponse.create(userApiService.signInUser(userEmail, userPass).execute()), listener);
            } catch (IOException e) {
                Timber.e(e);
                listener.onUserSignInResult(SignInStatus.Error());
            }
        });
    }

    private void manageResponse(ApiResponse<SignInResponse> response, @NonNull OnUserSignInResultListener listener) {
        if (response instanceof ApiSuccessResponse) {
            SignInResponse signInResponse = ((ApiSuccessResponse<SignInResponse>) response).getBody();
            if (signInResponse.getSuccess() == null || signInResponse.getSuccess()) {
                sessionStore.setUserSessionToken(signInResponse.getToken());
                listener.onUserSignInResult(SignInStatus.Success());
                Timber.d("Sign in success: " + ((ApiSuccessResponse) response).getBody());
            } else {
                listener.onUserSignInResult(SignInStatus.Error());
                Timber.d("Sign in error: " + ((ApiErrorResponse) response).getErrorMessage());
            }
        } else if (response instanceof ApiErrorResponse) {
            listener.onUserSignInResult(SignInStatus.Error());
            Timber.d("Sign in error: " + ((ApiErrorResponse) response).getErrorMessage());
        }
    }

    interface OnUserSignInResultListener {
        void onUserSignInResult(SignInStatus status);
    }
}
