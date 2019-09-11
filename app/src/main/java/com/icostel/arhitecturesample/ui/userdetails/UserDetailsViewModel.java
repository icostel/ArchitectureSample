package com.icostel.arhitecturesample.ui.userdetails;

import android.os.Bundle;

import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.domain.usecases.UserUseCase;
import com.icostel.arhitecturesample.view.mapper.UserMapper;
import com.icostel.arhitecturesample.view.model.User;
import com.icostel.commons.navigation.FragmentNavigationAction;
import com.icostel.commons.navigation.NavigationAction;
import com.icostel.commons.utils.livedata.SingleLiveEvent;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
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
    private UserUseCase userUseCase;
    private UserMapper userMapper;
    private Disposable userDisposable;
    private SingleLiveEvent<NavigationAction> navigationAction = new SingleLiveEvent<>();

    @Inject
    UserDetailsViewModel(UserUseCase userUseCase, UserMapper userMapper) {
        userLiveData = new MutableLiveData<>();
        this.userUseCase = userUseCase;
        this.userMapper = userMapper;
    }

    void init(String userId) {
        userDisposable = userUseCase.getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(domainUser -> userLiveData.setValue(userMapper.mapDomainToView(domainUser)),
                        throwable -> Timber.d("Could not get user with id %s cause: %s", userId, throwable.getMessage()));
    }

    // we share the view model between the parent activity and the details fragment
    void navigateToFirstFragment(String userId) {

        if (userId != null && userId.length() > 0) {
            Bundle args = new Bundle();
            args.putString(Config.Data.USER_ID, userId);
            navigationAction.postValue(new FragmentNavigationAction.Builder()
                    .setTargetFragment(TargetFragment.UserDetails)
                    .setArguments(args)
                    .setShouldFinish(true)
                    .build());
        }
    }

    MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public SingleLiveEvent<NavigationAction> getNavigationAction() {
        return navigationAction;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (userDisposable != null && !userDisposable.isDisposed()) {
            userDisposable.dispose();
        }
    }

    private enum TargetFragment implements FragmentNavigationAction.TargetFragment {
        UserDetails;

        @Override
        public Class<? extends Fragment> getFragmentClass() {
            switch (this) {
                case UserDetails:
                    return UserDetailsFragment.class;
                default:
                    return UserDetailsFragment.class;
            }
        }
    }
}
