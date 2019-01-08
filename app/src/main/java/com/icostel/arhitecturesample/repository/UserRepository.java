package com.icostel.arhitecturesample.repository;

import com.icostel.arhitecturesample.api.UsersApi;
import com.icostel.arhitecturesample.api.model.User;
import com.icostel.arhitecturesample.api.session.SessionStore;
import com.icostel.arhitecturesample.db.UserDao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * The main repository for the users, this implements the repository pattern using rx
 */
@Singleton
public class UserRepository {

    private static final String TAG = UserRepository.class.getCanonicalName();
    private static final int API_DEBOUNCE_TIME = 500;

    private final UsersApi usersApi;
    private final UserDao userDao;
    private final SessionStore sessionStore;

    @Inject
    UserRepository(UsersApi usersApi, UserDao userDao, SessionStore sessionStore) {
        this.usersApi = usersApi;
        this.userDao = userDao;
        this.sessionStore = sessionStore;
    }

    public Observable<Boolean> addUser(User apiUser) {
        Timber.d("%s addUser() %s", TAG, apiUser.toString());

        return usersApi.addUser(sessionStore.getUserSessionToken(), apiUser)
                .doOnNext(userId -> {
                    if (userId != -1) {
                        Timber.d("%s user valid received", TAG);
                        apiUser.setId(String.valueOf(userId));
                        storeUserInDb(apiUser);
                    }
                })
                .map(userId -> userId != -1);
    }

    @SuppressWarnings("unchecked")
    public Observable<Optional<User>> getUserById(String userId) {
        Timber.d("%s getUserById() %s", TAG, userId);
        return getUserFromDb(sessionStore.getUserSessionToken());
    }

    // this is exposed to the view model
    @SuppressWarnings("unchecked")
    public Observable<List<User>> getAllUsers() {
        Timber.d("%s getAllUsers()", TAG);
        return Observable.concatArray(getUsersFromDb(), getUsersFromApi(sessionStore.getUserSessionToken()))
                .debounce(API_DEBOUNCE_TIME, TimeUnit.MILLISECONDS);
    }

    // store the new users in DB
    private void storeUsersInDb(List<User> users) {
        Schedulers.io().createWorker().schedule(() -> userDao.upsert(users));
    }

    // get the users from db
    private Observable<List<User>> getUsersFromDb() {
        return userDao.getUsers()
                .filter(users -> users != null && !users.isEmpty())
                .toObservable();
    }

    private Observable<Optional<User>> getUserFromDb(String userId) {
        return userDao.getUserById(userId)
                .filter(Objects::nonNull)
                .map(Optional::of)
                .toObservable();
    }

    // get users from api service
    private Observable<List<User>> getUsersFromApi(String token) {
        return usersApi.getUsers(token)
                .map(users -> {
                    storeUsersInDb(users);
                    return users;
                });
    }

    private void storeUserInDb(User user) {
        Schedulers.io().createWorker().schedule(() -> userDao.upsert(user));
    }

    // get user from api service
    private Observable<Optional<User>> getUserFromApi(String token, String userId) {
        return usersApi.getUsers(token)
                .map(users -> {
                    for (User user : users) {
                        if (user.getId().equalsIgnoreCase(userId)) {
                            storeUserInDb(user);
                            return Optional.of(user);
                        }
                    }
                    return Optional.empty();
                });
    }

}
