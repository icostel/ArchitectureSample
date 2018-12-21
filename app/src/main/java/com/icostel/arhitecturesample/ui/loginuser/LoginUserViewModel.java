package com.icostel.arhitecturesample.ui.loginuser;

import android.text.TextUtils;

import com.icostel.arhitecturesample.api.SignInStatus;
import com.icostel.arhitecturesample.navigation.AppScreenProvider;
import com.icostel.arhitecturesample.repository.UserLogInHandler;
import com.icostel.arhitecturesample.utils.livedata.SingleLiveEvent;
import com.icostel.commons.navigation.ActivityNavigationAction;
import com.icostel.commons.navigation.NavigationAction;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginUserViewModel extends ViewModel {

    private MutableLiveData<SignInStatus.Status> signInStatusLive;
    private SingleLiveEvent<NavigationAction> navigationAction;
    private UserLogInHandler userLogInHandler;
    private AppScreenProvider appScreenProvider;

    @Inject
    public LoginUserViewModel(UserLogInHandler userLogInHandler, AppScreenProvider appScreenProvider) {
        this.signInStatusLive = new MutableLiveData<>();
        this.navigationAction = new SingleLiveEvent<>();
        this.userLogInHandler = userLogInHandler;
        this.appScreenProvider = appScreenProvider;
        this.signInStatusLive.setValue(SignInStatus.Status.NOT_STARTED);
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
            signInStatusLive.setValue(SignInStatus.Status.INPUTS_ERROR);
        } else {
            signInStatusLive.setValue(SignInStatus.Status.INPUTS_ERROR);
            userLogInHandler.signInUser(userEmail, userPass, signInStatus -> signInStatusLive.postValue(signInStatus.getStatus()));
        }
    }

    void onLoginSuccess() {
        navigationAction.postValue(
                new ActivityNavigationAction.Builder()
                        .setScreenProvider(appScreenProvider)
                        .setScreen(AppScreenProvider.LIST_USERS)
                        .setShouldFinish(true)
                        .build()
        );
    }
}
