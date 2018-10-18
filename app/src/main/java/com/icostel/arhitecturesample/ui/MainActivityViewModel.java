package com.icostel.arhitecturesample.ui;

import com.icostel.arhitecturesample.BuildConfig;
import com.icostel.arhitecturesample.model.User;
import com.icostel.arhitecturesample.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainActivityViewModel extends ViewModel {

    private final UserRepository userRepository;
    private MutableLiveData<List<User>> userListLiveData = new MutableLiveData<>();
    private Disposable userDisposable;

    @Inject
    MainActivityViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userListLiveData.setValue(new ArrayList<>());
        userDisposable = userRepository.getAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userListOptional -> {
                    if (userListOptional.isPresent()) {
                        if (userListOptional.get().size() > 0) {
                            userListLiveData.setValue(userListOptional.get());
                            if (BuildConfig.DEBUG) {
                                for (User u : userListLiveData.getValue()) {
                                    Timber.d(" received user: " + u.toString());
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
}
