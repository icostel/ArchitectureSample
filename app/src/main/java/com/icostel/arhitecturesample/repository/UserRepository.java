package com.icostel.arhitecturesample.repository;

import com.icostel.arhitecturesample.api.UsersApi;
import com.icostel.arhitecturesample.api.utils.SessionStore;
import com.icostel.arhitecturesample.db.UserDao;
import com.icostel.arhitecturesample.model.User;

import java.util.List;
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

    @SuppressWarnings("unused")
    public Observable<Optional<User>> getUserById(String userId) {
        //TODO implement the same repo pattern for one user call
        Timber.d("%s getUserById() %d", TAG, userId);
        return this.usersApi.getUser(sessionStore.getUserSessionToken(), userId)
                .subscribeOn(Schedulers.io())
                .map(restUser -> {
                    restUser.ifPresent(userDao::insert);
                    return restUser;
                });
    }

    // this is exposed to the view model
    public Observable<List<User>> getAllUsers() {
        Timber.d("%s getAllUsers()", TAG);
        String userToken = sessionStore.getUserSessionToken();
        return Observable.concatArray(getUsersFromDb(), getUsersFromApi(userToken));
    }

    //REPO PATTERN

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

    // get users from api service
    private Observable<List<User>> getUsersFromApi(String token) {
        return usersApi.getUsers(token)
                .subscribeOn(Schedulers.io())
                .map(users -> {
                    storeUsersInDb(users);
                    return users;
                });
    }

}
