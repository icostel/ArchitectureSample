package com.icostel.arhitecturesample.api

import com.icostel.arhitecturesample.api.model.User
import io.reactivex.Observable
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

// User API used by user UserRepository to retrieve user information and store it

@Singleton
class UsersApi @Inject
internal constructor(private val apiService: UserApiService) {

    fun getUser(authToken: String, userId: String): Observable<Optional<User>> {
        return apiService.getUsers(authToken)
                .map { userResponse ->
                    userResponse.data?.let { users ->
                        val usersSize = users.size
                        Timber.d("$TAG getUsers() size: %d", usersSize)
                        if (usersSize > 0) {
                            for (u in users) {
                                if (u.id == userId) {
                                    apiService.getUser(authToken, userId)
                                }
                            }
                        }
                    }

                    Timber.w(TAG + "getUser() with id: %s not found", userId)
                    Optional.empty<User>()
                }
    }

    /*
    fun addUser(authToken: String, user: User): Observable<Int> {
        return apiService.addUser(authToken, user)
                .map(userResponse -> userResponse.getData().get(0));
    }
     */

    fun getUsers(authToken: String): Observable<List<User>> {
        return apiService.getUsers(authToken)
                .map { userResponse ->
                    userResponse.data?.let { users ->
                        val usersSize = users.size
                        Timber.d("$TAG getUsers() size: %d", usersSize)
                        if (usersSize != 0) {
                            apiService.getUsers(authToken)
                        }
                        emptyList<User>()
                    }
                }
    }

    companion object {
        private val TAG = UsersApi::class.java.canonicalName
    }
}
