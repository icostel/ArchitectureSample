package com.icostel.arhitecturesample.ui.listusers;

import android.os.Bundle;

import com.icostel.arhitecturesample.BuildConfig;
import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.api.Status;
import com.icostel.arhitecturesample.domain.UserHandler;
import com.icostel.arhitecturesample.navigation.AppScreenProvider;
import com.icostel.arhitecturesample.ui.newuser.NewUserActivity;
import com.icostel.arhitecturesample.view.mapper.UserMapper;
import com.icostel.arhitecturesample.view.model.User;
import com.icostel.commons.navigation.ActivityNavigationAction;
import com.icostel.commons.navigation.NavigationAction;
import com.icostel.commons.utils.livedata.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ListUsersViewModel extends ViewModel {

    private MutableLiveData<List<User>> userListLiveData = new MutableLiveData<>();
    private MutableLiveData<Status.Type> loadingStatus = new MutableLiveData<>();
    private SingleLiveEvent<NavigationAction> navigationActionLiveEvent = new SingleLiveEvent<>();
    private Disposable userDisposable;
    private UserHandler userHandler;
    private UserMapper userMapper;
    private AppScreenProvider appScreenProvider;

    @Inject
    ListUsersViewModel(UserHandler userHandler, UserMapper userMapper, AppScreenProvider appScreenProvider) {
        this.userListLiveData.setValue(new ArrayList<>());
        this.userHandler = userHandler;
        this.userMapper = userMapper;
        this.appScreenProvider = appScreenProvider;
        getUsers(userHandler, null);
    }

    // used in the UI for updating the user list
    MutableLiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }

    SingleLiveEvent<NavigationAction> getNavigationActionLiveEvent() {
        return navigationActionLiveEvent;
    }

    void onSearchInput(String searchInput) {
        getUsers(userHandler, searchInput);
    }

    private void getUsers(UserHandler userHandler, String nameQuery) {
        loadingStatus.setValue(Status.Type.IN_PROGRESS);

        userDisposable = userHandler.getAllUsers(nameQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(1000, TimeUnit.MILLISECONDS) // just for testing lag
                .subscribe(userList -> {
                    userListLiveData.postValue(userMapper.mapDomainToView(userList));
                    loadingStatus.postValue(Status.Type.SUCCESS);
                    if (BuildConfig.DEBUG) {
                        Timber.d("received %d users ", userList.size());
                        if (userListLiveData.getValue() != null) {
                            for (User u : userListLiveData.getValue()) {
                                Timber.d("user: " + u.toString());
                            }
                        }
                    }
                }, throwable -> {
                    Timber.e("Could not get users: " + throwable);
                    loadingStatus.setValue(Status.Type.ERROR);
                });
    }

    MutableLiveData<Status.Type> getLoadingStatus() {
        return loadingStatus;
    }

    Status.Type getLoadingStatusInstant() {
        return loadingStatus.getValue();
    }

    void refreshUsers(String query) {
        getUsers(this.userHandler, query);
        loadingStatus.setValue(Status.Type.IN_PROGRESS);
    }

    public void onUserAdd(Bundle transitionBundle) {
        Timber.d("onUserAdd()");

        navigationActionLiveEvent.postValue(new ActivityNavigationAction.Builder()
                .setScreenProvider(appScreenProvider)
                .setScreen(AppScreenProvider.NEW_USER)
                .setRequestCode(NewUserActivity.RESULT_CODE_USER_ADDED)
                .setTransitionBundle(transitionBundle)
                .setShouldFinish(false)
                .build());
    }

    // navigate to details when the user selects a specific user from the list
    void onUserSelected(User user) {
        Timber.d("onUserSelected()");

        Bundle args = new Bundle();
        args.putString(Config.Data.USER_ID, user.getId());
        navigationActionLiveEvent.postValue(
                new ActivityNavigationAction.Builder()
                        .setScreen(AppScreenProvider.USER_DETAILS)
                        .setBundle(args)
                        .setShouldFinish(false)
                        .setScreenProvider(appScreenProvider)
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
