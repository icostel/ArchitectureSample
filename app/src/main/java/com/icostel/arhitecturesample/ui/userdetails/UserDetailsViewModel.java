package com.icostel.arhitecturesample.ui.userdetails;

import com.icostel.arhitecturesample.api.model.User;
import com.icostel.arhitecturesample.repository.UserRepository;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


/**
 * View model for one single user, used by {@link com.icostel.arhitecturesample.ui.userdetails.UserDetailsActivity}
 */
public class UserDetailsViewModel extends ViewModel {

    private MutableLiveData<User> userLiveData;
    private UserRepository userRepository;

    @Inject
    public UserDetailsViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
