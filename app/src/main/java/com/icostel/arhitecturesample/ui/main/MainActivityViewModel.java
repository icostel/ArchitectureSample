package com.icostel.arhitecturesample.ui.main;

import android.os.Bundle;

import com.icostel.arhitecturesample.BuildConfig;
import com.icostel.arhitecturesample.model.User;
import com.icostel.arhitecturesample.navigation.ActivityNavigationAction;
import com.icostel.arhitecturesample.navigation.NavigationAction;
import com.icostel.arhitecturesample.repository.UserRepository;
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsActivity;
import com.icostel.arhitecturesample.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<User>> userListLiveData = new MutableLiveData<>();
    private SingleLiveEvent<NavigationAction> navigatioActionLiveEvent = new SingleLiveEvent<>();
    private Disposable userDisposable;

    @Inject
    MainActivityViewModel(UserRepository userRepository) {
        this.userListLiveData.setValue(new ArrayList<>());
        userDisposable = userRepository.getAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userListOptional -> {
                    if (userListOptional.isPresent()) {
                        if (userListOptional.get().size() > 0) {
                            userListLiveData.setValue(userListOptional.get());
                            if (BuildConfig.DEBUG) {
                                if (userListLiveData.getValue() != null) {
                                    for (User u : userListLiveData.getValue()) {
                                        Timber.d(" received user: " + u.toString());
                                    }
                                }
                            }
                            Timber.d("received %d users ", userListOptional.get().size());
                        }
                    }
                }, throwable -> Timber.e("Could not get users: " + throwable));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (userDisposable != null && !userDisposable.isDisposed()) {
            userDisposable.dispose();
        }
    }

    MutableLiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }

    SingleLiveEvent<NavigationAction> getNavigatioActionLiveEvent() {
        return navigatioActionLiveEvent;
    }

    void onUserSelected(User user) {
        Bundle extras = new Bundle();
        extras.putString(UserDetailsActivity.EXTRA_USER_ID, user.getId());
        navigatioActionLiveEvent.postValue(new ActivityNavigationAction.Builder()
                .setScreen(ActivityNavigationAction.Screen.UserDetais)
                .setBundle(extras)
                .setShouldFinish(false)
                .build());
    }
}
