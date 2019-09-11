package com.icostel.arhitecturesample.repository

import android.text.TextUtils

import com.icostel.arhitecturesample.api.UsersApi
import com.icostel.arhitecturesample.api.model.User
import com.icostel.arhitecturesample.api.session.SessionStore
import com.icostel.arhitecturesample.db.UserDao
import com.icostel.commons.utils.AppExecutors
import java.util.Objects
import java.util.Optional
import java.util.concurrent.TimeUnit

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * The main repository for the users, this implements the repository pattern using rx
 */
@Singleton
class UserRepository @Inject
internal constructor(private val usersApi: UsersApi,
                     private val userDao: UserDao,
                     private val appExecutors: AppExecutors,
                     private val sessionStore: SessionStore) {

    fun addUser(apiUser: User): Observable<Boolean> {
        Timber.d("%s addUser() %s", TAG, apiUser.toString())

        /*
        return usersApi.addUser(sessionStore.getUserSessionToken(), apiUser)
                .doOnNext(userId -> {
                    if (userId != -1) {
                        Timber.d("%s user valid received", TAG);
                        apiUser.setId(String.valueOf(userId));
                        storeUserInDb(apiUser);
                    }
                })
                .map(userId -> userId != -1);
         */
        return Observable.just(true)
    }

    fun getUserById(userId: String): Observable<Optional<User>> {
        Timber.d("%s getUserById() %s", TAG, userId)
        return getUserFromDb(userId)
    }

    // this is exposed to the view model
    fun getAllUsers(nameQuery: String): Observable<List<User>> {
        return if (TextUtils.isEmpty(nameQuery)) {
            Timber.d("%s getAllUsers()", TAG)
            Observable.concatArray(getUsersFromDb(""),
                    getUsersFromApi(sessionStore.userSessionToken))
                    .debounce(API_DEBOUNCE_TIME.toLong(), TimeUnit.MILLISECONDS)
        } else {
            // get the users from DB
            getUsersFromDb(nameQuery)
        }
    }

    // store the new users in DB
    private fun storeUsersInDb(users: List<User>) {
        appExecutors.diskIO().execute {
            for (user in users) {
                userDao.upsert(user)
            }
        }
    }

    // get the users from db
    private fun getUsersFromDb(nameQuery: String): Observable<List<User>> {
        // append % to the beginning and end of the query
        return userDao.getUsers(nameQuery)
                .filter { users -> users.isNotEmpty() }
                .toObservable()
    }

    private fun getUserFromDb(userId: String): Observable<Optional<User>> {
        Timber.d("getUserFromDb(id=%s)", userId)
        return userDao.getUserById(userId)
                .filter { Objects.nonNull(it) }
                .map { Optional.of(it) }
                .toObservable()
    }

    // get users from api service
    private fun getUsersFromApi(token: String?): Observable<List<User>> {
        return usersApi.getUsers(token!!)
                .map { users ->
                    storeUsersInDb(users)
                    users
                }
    }

    private fun storeUserInDb(user: User) {
        appExecutors.diskIO().execute { userDao.upsert(user) }
        Schedulers.io().createWorker().schedule { userDao.upsert(user) }
    }

    // get user from api service
    private fun getUserFromApi(token: String, userId: String): Observable<Optional<User>> {
        return usersApi.getUsers(token)
                .map { users ->
                    for (user in users) {
                        if (user.id.equals(userId, ignoreCase = true)) {
                            storeUserInDb(user)
                            usersApi.getUser(token, userId)
                        }
                    }
                    Optional.empty<User>()
                }
    }

    companion object {
        private val TAG = UserRepository::class.java.canonicalName
        private const val API_DEBOUNCE_TIME = 500
    }

}
