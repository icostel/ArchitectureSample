package com.icostel.arhitecturesample.repository;

import com.icostel.arhitecturesample.api.UsersApi;
import com.icostel.arhitecturesample.db.UserDao;
import com.icostel.arhitecturesample.model.User;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class UserRepository {

    private static final String TAG = UserRepository.class.getCanonicalName();

    private final UsersApi usersApi;
    private final UserDao userDao;

    @Inject
    UserRepository(UsersApi usersApi, UserDao userDao) {
        this.usersApi = usersApi;
        this.userDao = userDao;
    }

    public Observable<Optional<User>> getUser(String userId) {
        //get token from somewhere
        final String token = "user_token";
        return this.usersApi.getUser(token, userId)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Optional<List<User>>> getAllUsers() {
        //get token from somewhere, a session manager maybe
        final String token = "user_token";
        return this.usersApi.getUsers(token)
                .subscribeOn(Schedulers.io())
                .map(users -> {
                    // insert or update all users on call
                    users.ifPresent(userDao::upsert);
                    return users;
                });
    }
}
