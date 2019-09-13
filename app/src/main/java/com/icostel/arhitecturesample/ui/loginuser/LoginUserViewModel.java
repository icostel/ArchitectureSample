package com.icostel.arhitecturesample.ui.loginuser;

import android.os.Handler;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.icostel.arhitecturesample.api.Status;
import com.icostel.arhitecturesample.navigation.AppScreenProvider;
import com.icostel.arhitecturesample.repository.UserLogInHandler;
import com.icostel.commons.navigation.ActivityNavigationAction;
import com.icostel.commons.navigation.NavigationAction;
import com.icostel.commons.utils.livedata.SingleLiveEvent;

import javax.inject.Inject;

public class LoginUserViewModel extends ViewModel {

    private MutableLiveData<Status.Type> signInStatusLive;
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
        this.signInStatusLive.postValue(Status.Type.NOT_STARTED);
    }

    // used in the UI for displaying the log in status
    MutableLiveData<Status.Type> getSignInStatusLive() {
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
            signInStatusLive.setValue(Status.Type.INPUTS_ERROR);
        } else {
            signInStatusLive.setValue(Status.Type.SUCCESS);
            userLogInHandler.signInUser(userEmail, userPass, status -> signInStatusLive.postValue(status.getType()));
        }
    }

    void onLoginSuccess() {
        new Handler().postDelayed(() ->
                navigationAction.postValue(
                        new ActivityNavigationAction.Builder()
                                .setScreenProvider(appScreenProvider)
                                .setScreen(AppScreenProvider.MAIN)
                                .setShouldFinish(true)
                                .build()
                ), 2000L);
    }
}
