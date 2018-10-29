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
        // if we have the user in the DB, return it from there
        // if not do the API call, insert the user in DB and return it
        // TODO concat with db call

        Timber.d("%s getUserById() %d", TAG, userId);
        return this.usersApi.getUser(sessionStore.getUserSessionToken(), userId)
                .subscribeOn(Schedulers.io())
                .map(restUser -> {
                    restUser.ifPresent(userDao::insert);
                    return restUser;
                });
        /*
        return Observable.just(userDao.getUserById(userId))
                .subscribeOn(Schedulers.io())
                .flatMap(dbUser -> {
                    if (dbUser != null) {
                        return Observable.just(Optional.of(dbUser));
                    } else {
                        return this.usersApi.getUser(sessionStore.getUserSessionToken(), userId)
                                .subscribeOn(Schedulers.io())
                                .map(restUser -> {
                                    restUser.ifPresent(userDao::insert);
                                    return restUser;
                                });
                    }
                });
                */
    }

    public Observable<Optional<List<User>>> getAllUsers() {
        // if we have users in the dao, return from there
        // if not do the API call, insert users in DB and return them
        // TODO concat with db call

        Timber.d("%s getAllUsers()", TAG);

        return this.usersApi.getUsers(sessionStore.getUserSessionToken())
                .subscribeOn(Schedulers.io())
                .map(users -> {
                    // insert or update all users on call
                    users.ifPresent(userDao::upsert);
                    return users;
                });
        /*
        return Observable.just(userDao.getUsers())
                .subscribeOn(Schedulers.io())
                .flatMap(daoUsers -> {
                    if (daoUsers != null && daoUsers.size() > 0) {
                        return Observable.just(Optional.of(daoUsers));
                    } else {
                        return this.usersApi.getUsers(sessionStore.getUserSessionToken())
                                .subscribeOn(Schedulers.io())
                                .map(users -> {
                                    // insert or update all users on call
                                    users.ifPresent(userDao::upsert);
                                    return users;
                                });
                    }
                });
                */
    }

}
