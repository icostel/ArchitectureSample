package com.icostel.arhitecturesample.ui.userdetails;

import com.icostel.arhitecturesample.domain.UserHandler;
import com.icostel.arhitecturesample.view.model.User;
import com.icostel.arhitecturesample.view.mapper.UserMapper;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


/**
 * View model for one single user, used by {@link com.icostel.arhitecturesample.ui.userdetails.UserDetailsActivity}
 */
public class UserDetailsViewModel extends ViewModel {

    private MutableLiveData<User> userLiveData;
    private UserHandler userHandler;
    private UserMapper userMapper;
    private Disposable userDisposable;

    @Inject
    UserDetailsViewModel(UserHandler userHandler, UserMapper userMapper) {
        userLiveData = new MutableLiveData<>();
        this.userHandler = userHandler;
        this.userMapper = userMapper;
    }

    void init(String userId) {
        userDisposable = userHandler.getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(domainUser -> userLiveData.setValue(userMapper.mapDomainToView(domainUser)),
                        throwable -> Timber.d("Could not get user with id %s cause: %s", userId, throwable.getMessage()));
    }

    MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (userDisposable != null && !userDisposable.isDisposed()) {
            userDisposable.dispose();
        }
    }
}
