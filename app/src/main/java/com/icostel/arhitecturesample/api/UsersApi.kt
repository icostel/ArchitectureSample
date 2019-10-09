package com.icostel.arhitecturesample.api

import com.icostel.arhitecturesample.api.model.User
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

// User API used by user UserRepository to retrieve user information and store it

@Singleton
class UsersApi @Inject
internal constructor(private val apiService: UserApiService) {

    /*
    fun getUser(authToken: String, userId: String): Observable<Optional<User>> {
        return apiService.getUsers(authToken).data
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
     */

    fun getUsers(authToken: String?): List<User> {
        if (authToken != null) {
            val res = apiService.getUsers(authToken).execute()
            Timber.d("$TAG getUsers() api result: ${res.isSuccessful}")
            if (res.isSuccessful) {
                return res.body()?.data ?: emptyList()
            }
        }

        return emptyList()
    }

    companion object {
        private val TAG = UsersApi::class.java.canonicalName
    }
}
