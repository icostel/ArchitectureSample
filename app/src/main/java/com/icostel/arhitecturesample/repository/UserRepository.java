package com.icostel.arhitecturesample.repository;

import com.icostel.arhitecturesample.api.UsersApi;
import com.icostel.arhitecturesample.api.session.SessionStore;
import com.icostel.arhitecturesample.db.UserDao;
import com.icostel.arhitecturesample.api.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    private final UsersApi usersApi;
    private final UserDao userDao;
    private final SessionStore sessionStore;

    @Inject
    UserRepository(UsersApi usersApi, UserDao userDao, SessionStore sessionStore) {
        this.usersApi = usersApi;
        this.userDao = userDao;
        this.sessionStore = sessionStore;
    }

    ///////////REPO PATTERN API

    @SuppressWarnings("unchecked")
    public Observable<Optional<User>> getUserById(String userId) {
        Timber.d("%s getUserById() %s", TAG, userId);
        String userToken = sessionStore.getUserSessionToken();
        return Observable.concatArray(getUserFromDb(userId), getUserFromApi(userToken, userId));
    }

    // this is exposed to the view model
    @SuppressWarnings("unchecked")
    public Observable<List<User>> getAllUsers() {
        Timber.d("%s getAllUsers()", TAG);
        String userToken = sessionStore.getUserSessionToken();
        return Observable.concatArray(getUsersFromDb(), getUsersFromApi(userToken));
    }

    ///////////REPO PATTERN API

    //SINGLE USER API

    // store the new users in DB
    private void storeUsersInDb(List<User> users) {
        Schedulers.io().createWorker().schedule(() -> userDao.upsert(users));
    }

    // get the users from db
    private Observable<List<User>> getUsersFromDb() {
        return userDao.getUsers()
                .subscribeOn(Schedulers.io())
                .filter(users -> users != null && !users.isEmpty())
                .toObservable();
    }

    private Observable<Optional<User>> getUserFromDb(String userId) {
        return userDao.getUserById(userId)
                .subscribeOn(Schedulers.io())
                .filter(Objects::nonNull)
                .map(Optional::of)
                .toObservable();
    }

    //MORE USERS API

    // get users from api service
    private Observable<List<User>> getUsersFromApi(String token) {
        return usersApi.getUsers(token)
                .subscribeOn(Schedulers.io())
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
                .subscribeOn(Schedulers.io())
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
