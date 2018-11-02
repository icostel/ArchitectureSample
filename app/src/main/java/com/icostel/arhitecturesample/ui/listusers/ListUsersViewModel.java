package com.icostel.arhitecturesample.ui.listusers;

import android.os.Bundle;

import com.icostel.arhitecturesample.BuildConfig;
import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.api.model.User;
import com.icostel.arhitecturesample.navigation.ActivityNavigationAction;
import com.icostel.arhitecturesample.navigation.NavigationAction;
import com.icostel.arhitecturesample.repository.UserRepository;
import com.icostel.arhitecturesample.utils.livedata.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ListUsersViewModel extends ViewModel {

    private MutableLiveData<List<User>> userListLiveData = new MutableLiveData<>();
    private SingleLiveEvent<NavigationAction> navigationActionLiveEvent = new SingleLiveEvent<>();
    private Disposable userDisposable;

    @Inject
    ListUsersViewModel(UserRepository userRepository) {
        this.userListLiveData.setValue(new ArrayList<>());
        getUsers(userRepository);
    }

    // used in the UI for updating the user list
    MutableLiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }

    SingleLiveEvent<NavigationAction> getNavigationActionLiveEvent() {
        return navigationActionLiveEvent;
    }

    private void getUsers(UserRepository userRepository) {
        userDisposable = userRepository.getAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> {
                    if (userList.size() > 0) {
                        userListLiveData.setValue(userList);
                        if (BuildConfig.DEBUG) {
                            Timber.d("received %d users ", userList.size());
                            if (userListLiveData.getValue() != null) {
                                for (User u : userListLiveData.getValue()) {
                                    Timber.d("user: " + u.toString());
                                }
                            }
                        }
                    }
                }, throwable -> Timber.e("Could not get users: " + throwable));
    }

    // navigate to details when the user select a specific user from the list
    void onUserSelected(User user) {
        Bundle extras = new Bundle();
        extras.putString(Config.Data.USER_ID, user.getId());
        navigationActionLiveEvent.postValue(new ActivityNavigationAction.Builder()
                .setScreen(ActivityNavigationAction.Screen.UserDetais)
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
