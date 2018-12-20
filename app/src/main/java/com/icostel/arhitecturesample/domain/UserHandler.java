package com.icostel.arhitecturesample.domain;

import com.icostel.arhitecturesample.domain.model.User;
import com.icostel.arhitecturesample.domain.model.UserMapper;
import com.icostel.arhitecturesample.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

// used a intermediate level between repository and view (domain related)
@Singleton
public class UserHandler {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Inject
    public UserHandler(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Observable<List<User>> getAllUsers() {
        return userRepository.getAllUsers().map(userMapper::mapApiToDomain);
    }

    public Observable<User> getUser(String userId) {
        return userRepository.getUserById(userId).filter(Optional::isPresent).map(Optional::get).map(userMapper::mapApiToDomain);
    }
}
