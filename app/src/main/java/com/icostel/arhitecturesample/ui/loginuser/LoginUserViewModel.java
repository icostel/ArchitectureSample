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
    private MutableLiveData<Boolean> allInputsAvailable;
    private SingleLiveEvent<NavigationAction> navigationAction;
    private UserLogInHandler userLogInHandler;
    private AppScreenProvider appScreenProvider;

    @Inject
    LoginUserViewModel(UserLogInHandler userLogInHandler, AppScreenProvider appScreenProvider) {
        this.signInStatusLive = new MutableLiveData<>();
        this.navigationAction = new SingleLiveEvent<>();
        this.allInputsAvailable = new MutableLiveData<>();
        this.allInputsAvailable.postValue(false);
        this.userLogInHandler = userLogInHandler;
        this.appScreenProvider = appScreenProvider;
        this.signInStatusLive.postValue(SignInStatus.Status.NOT_STARTED);
    }

    // used in the UI for displaying the log in status
    MutableLiveData<SignInStatus.Status> getSignInStatusLive() {
        return signInStatusLive;
    }

    // used for navigation
    SingleLiveEvent<NavigationAction> getNavigationAction() {
        return navigationAction;
    }

    // used for enabling/disabling the log in btn
    MutableLiveData<Boolean> getAllInputsAvailable() {
        return allInputsAvailable;
    }

    void allInputsAvailable(CharSequence userEmail, CharSequence userPass) {
        allInputsAvailable.postValue(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass));
    }

    // the log in
    void onLogInBtnClicked(String userEmail, String userPass) {
        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPass)) {
            signInStatusLive.setValue(SignInStatus.Status.INPUTS_ERROR);
        } else {
            signInStatusLive.setValue(SignInStatus.Status.SUCCESS);
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
