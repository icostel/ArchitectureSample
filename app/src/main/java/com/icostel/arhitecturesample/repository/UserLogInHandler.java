package com.icostel.arhitecturesample.repository;

import com.icostel.arhitecturesample.api.UserApiService;
import com.icostel.arhitecturesample.api.model.SignInResponse;
import com.icostel.arhitecturesample.api.utils.ApiErrorResponse;
import com.icostel.arhitecturesample.api.utils.ApiResponse;
import com.icostel.arhitecturesample.api.utils.ApiSuccessResponse;
import com.icostel.arhitecturesample.api.utils.SessionStore;
import com.icostel.arhitecturesample.api.utils.SignInStatus;
import com.icostel.arhitecturesample.utils.AppExecutors;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class UserLogInHandler {

    private UserApiService userApiService;
    private AppExecutors appExecutors;
    private SessionStore sessionStore;

    @Inject
    UserLogInHandler(AppExecutors executors, UserApiService userApiService, SessionStore sessionStore) {
        this.userApiService = userApiService;
        this.appExecutors = executors;
        this.sessionStore = sessionStore;
    }

    // other API calls will use the token obtained from user sign in and stored in session store
    public void signInUser(@NonNull String userEmail, @NonNull String userPass,
                           @NonNull OnUserSignInResultListener listener) {
        // add a delay to simulate a bad network or something, a little nasty to read tho
        Schedulers.io().createWorker().schedule(() ->
                appExecutors.networkIO().execute(() -> {
                    try {
                        manageResponse(ApiResponse.create(userApiService.signInUser(userEmail, userPass).execute()), listener);
                    } catch (Exception e) {
                        Timber.e(e);
                        listener.onUserSignInResult(SignInStatus.Error());
                    }
                }), 3000, TimeUnit.MILLISECONDS);
    }

    private void manageResponse(ApiResponse<SignInResponse> response, @NonNull OnUserSignInResultListener listener) {
        if (response instanceof ApiSuccessResponse) {
            SignInResponse signInResponse = ((ApiSuccessResponse<SignInResponse>) response).getBody();
            if (signInResponse.getSuccess()) {
                sessionStore.setUserSessionToken(signInResponse.getToken());
                listener.onUserSignInResult(SignInStatus.Success());
                Timber.d("===Sign in success: " + ((ApiSuccessResponse) response).getBody());
            } else {
                listener.onUserSignInResult(SignInStatus.Error());
                Timber.d("===Sign in error: " + ((ApiErrorResponse) response).getErrorMessage());
            }
        } else if (response instanceof ApiErrorResponse) {
            listener.onUserSignInResult(SignInStatus.Error());
            Timber.d("===Sign in error: " + ((ApiErrorResponse) response).getErrorMessage());
        }
    }

    public interface OnUserSignInResultListener {
        void onUserSignInResult(SignInStatus status);
    }
}
