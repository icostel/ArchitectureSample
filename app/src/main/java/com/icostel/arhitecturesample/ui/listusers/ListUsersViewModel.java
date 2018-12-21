package com.icostel.arhitecturesample.ui.listusers;

import android.os.Bundle;

import com.icostel.arhitecturesample.BuildConfig;
import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.api.SignInStatus;
import com.icostel.arhitecturesample.domain.UserHandler;
import com.icostel.arhitecturesample.navigation.AppScreenProvider;
import com.icostel.arhitecturesample.utils.livedata.SingleLiveEvent;
import com.icostel.arhitecturesample.view.model.User;
import com.icostel.arhitecturesample.view.model.UserMapper;
import com.icostel.commons.navigation.ActivityNavigationAction;
import com.icostel.commons.navigation.NavigationAction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ListUsersViewModel extends ViewModel {

    private MutableLiveData<List<User>> userListLiveData = new MutableLiveData<>();
    private SingleLiveEvent<NavigationAction> navigationActionLiveEvent = new SingleLiveEvent<>();
    private Disposable userDisposable;
    private UserHandler userHandler;
    private UserMapper userMapper;
    private MutableLiveData<SignInStatus.Status> loadingStatus = new MutableLiveData<>();
    private AppScreenProvider appScreenProvider;

    @Inject
    ListUsersViewModel(UserHandler userHandler, UserMapper userMapper, AppScreenProvider appScreenProvider) {
        this.userListLiveData.setValue(new ArrayList<>());
        this.userHandler = userHandler;
        this.userMapper = userMapper;
        this.appScreenProvider = appScreenProvider;
        getUsers(userHandler);
    }

    // used in the UI for updating the user list
    MutableLiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }

    SingleLiveEvent<NavigationAction> getNavigationActionLiveEvent() {
        return navigationActionLiveEvent;
    }

    private void getUsers(UserHandler userHandler) {
        loadingStatus.setValue(SignInStatus.Status.IN_PROGRESS);

        userDisposable = userHandler.getAllUsers()
                .delay(3000, TimeUnit.MILLISECONDS) // just for testing lag
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> {
                    if (userList.size() > 0) {
                        userListLiveData.setValue(userMapper.mapDomainToView(userList));
                        loadingStatus.postValue(SignInStatus.Status.SUCCESS);
                        if (BuildConfig.DEBUG) {
                            Timber.d("received %d users ", userList.size());
                            if (userListLiveData.getValue() != null) {
                                for (User u : userListLiveData.getValue()) {
                                    Timber.d("user: " + u.toString());
                                }
                            }
                        }
                    }
                }, throwable -> {
                    Timber.e("Could not get users: " + throwable);
                    loadingStatus.setValue(SignInStatus.Status.ERROR);
                });
    }

    MutableLiveData<SignInStatus.Status> getLoadingStatus() {
        return loadingStatus;
    }

    void refreshUsers() {
        getUsers(this.userHandler);
        loadingStatus.setValue(SignInStatus.Status.IN_PROGRESS);
    }

    void addUser(@NonNull User user) {
        //TODO open the add user screen
    }

    // navigate to details when the user select a specific user from the list
    void onUserSelected(User user) {
        Bundle extras = new Bundle();
        extras.putString(Config.Data.USER_ID, user.getId());
        navigationActionLiveEvent.postValue(new ActivityNavigationAction.Builder()
                .setScreenProvider(appScreenProvider)
                .setScreen(AppScreenProvider.USER_DETAILS)
                .setBundle(extras)
                .setShouldFinish(false)
                .build());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (userDisposable != null && !userDisposable.isDisposed()) {
            userDisposable.dispose();
        }
    }
}
