package com.icostel.arhitecturesample.repository

import com.icostel.arhitecturesample.api.UsersApi
import com.icostel.arhitecturesample.api.model.User
import com.icostel.arhitecturesample.api.session.SessionStore
import com.icostel.arhitecturesample.db.UserDao
import com.icostel.commons.utils.AppExecutors
import io.reactivex.Observable
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The main repository for the users
 */
@Singleton
class UserRepository @Inject
internal constructor(private val usersApi: UsersApi,
                     private val userDao: UserDao,
                     private val appExecutors: AppExecutors,
                     private val sessionStore: SessionStore) : IRepository() {

    fun addUser(apiUser: User): Observable<Boolean> {
        // for now we're only adding the new user in the db, as we don't really have a BE
        return storeUserInDb(apiUser)
    }

    fun getUserById(userId: String): Observable<Optional<User>> {
        return getUserFromDb(userId)
    }

    fun getAllUsersObservable(nameQuery: String?): Observable<List<User>> {
        return Observable.fromCallable {
            getAllUsers(nameQuery)
        }
    }

    fun getAllUsers(nameQuery: String?): List<User> {
        // we already have users in the db
        return if (userDao.getUserCount() > 0) {
            if (nameQuery.isNullOrEmpty()) {
                getUsersFromDb()
            } else {
                getUsersFromDb(nameQuery)
            }
        } else {
            val users = getUsersFromApi()
            storeUsersInDb(users)
            users
        }
    }

    // store the new users in DB
    fun storeUsersInDb(users: List<User>?) {
        if (users == null) {
            Timber.d("$TAG storeUsersInDb() user list is null, skipping...")
        } else {
            appExecutors.diskIO().execute {
                for (user in users) {
                    userDao.upsert(user)
                }
            }
        }
    }

    // get the users from db
    private fun getUsersFromDb(): List<User> {
        // append % to the beginning and end of the query
        return userDao.getUsers()
    }

    // get the users from db
    private fun getUsersFromDb(query: String): List<User> {
        // append % to the beginning and end of the query
        return userDao.getUsers("$query%")
    }

    private fun getUserFromDb(userId: String): Observable<Optional<User>> {
        return userDao.getUserById(userId)
                .filter { Objects.nonNull(it) }
                .map { Optional.of(it) }
                .toObservable()
    }

    // get users from api service
    fun getUsersFromApi(): List<User> {
        return usersApi.getUsers(sessionStore.getUserToken())
    }

    private fun storeUserInDb(user: User): Observable<Boolean> {
        return Observable.fromCallable {
            Timber.d("$TAG storing user $user in db")
            userDao.upsert(user)
            true
        }
    }

    // get user from api service
    /*
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
     */

    companion object {
        private val TAG = UserRepository::class.java.canonicalName
    }

}
