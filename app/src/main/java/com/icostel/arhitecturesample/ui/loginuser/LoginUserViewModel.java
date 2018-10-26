package com.icostel.arhitecturesample.ui.loginuser;

import android.text.TextUtils;

import com.icostel.arhitecturesample.api.utils.SignInStatus;
import com.icostel.arhitecturesample.navigation.ActivityNavigationAction;
import com.icostel.arhitecturesample.navigation.NavigationAction;
import com.icostel.arhitecturesample.repository.UserLogInHandler;
import com.icostel.arhitecturesample.utils.SingleLiveEvent;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LoginUserViewModel extends ViewModel {

    private MutableLiveData<SignInStatus.Status> signInStatusLive;
    private SingleLiveEvent<NavigationAction> navigationAction;
    private final UserLogInHandler userLogInHandler;

    @Inject
    LoginUserViewModel(UserLogInHandler userLogInHandler) {
        this.signInStatusLive = new MutableLiveData<>();
        this.navigationAction = new SingleLiveEvent<>();
        this.userLogInHandler = userLogInHandler;
        this.signInStatusLive.setValue(SignInStatus.Status.NotStarted);
    }

    // used in the UI for displaying the log in status
    MutableLiveData<SignInStatus.Status> getSignInStatusLive() {
        return signInStatusLive;
    }

    // used for navigation
    SingleLiveEvent<NavigationAction> getNavigationAction() {
        return navigationAction;
    }

    // the log in
    void onLogInBtnClicked(String userEmail, String userPass) {
        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPass)) {
            signInStatusLive.setValue(SignInStatus.Status.InputsError);
        } else {
            signInStatusLive.setValue(SignInStatus.Status.InProgress);
            userLogInHandler.signInUser(userEmail, userPass,
                    signInStatus ->
                            AndroidSchedulers.mainThread()
                                    .createWorker()
                                    .schedule(() -> signInStatusLive.setValue(signInStatus.getStatus())));
        }
    }

    void onLoginSuccess() {
        navigationAction.postValue(
                new ActivityNavigationAction.Builder()
                        .setScreen(ActivityNavigationAction.Screen.ListUsers)
                        .setShouldFinish(true)
                        .build()
        );
    }
}
