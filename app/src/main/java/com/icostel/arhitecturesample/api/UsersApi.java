package com.icostel.arhitecturesample.api;

import com.icostel.arhitecturesample.api.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import timber.log.Timber;

// User API used by user UserRepository to retrieve user information and store it in the DB

@Singleton
public class UsersApi {

    private static final String TAG = UsersApi.class.getCanonicalName();

    private UserApiService apiService;

    @Inject
    UsersApi(UserApiService userApiService) {
        this.apiService = userApiService;
    }

    public Observable<Optional<User>> getUser(final String authToken, final String userId) {
        return apiService.getUsers(authToken)
                .map(userResponse -> {
                    int usersSize = userResponse.getData().size();
                    Timber.d(TAG + " getUsers() size: %d", usersSize);
                    if (usersSize > 0) {
                        for (User u : userResponse.getData()) {
                            if (u.getId().equals(userId)) {
                                return Optional.of(u);
                            }
                        }
                    }

                    Timber.w(TAG + "getUser() with id: %s not found", userId);
                    return Optional.empty();
                });
    }

    public Observable<Integer> addUser(final String authToken, User user) {
        return apiService.addUser(authToken, user)
                .map(userResponse -> userResponse.getData().get(0));
    }

    // maps the user response to the user list as we don't use the REST object in calling API
    public Observable<List<User>> getUsers(final String authToken) {
        return apiService.getUsers(authToken)
                .map(userResponse -> {
                    int usersSize = userResponse.getData().size();
                    Timber.d(TAG + " getUsers() size: %d", usersSize);
                    if (usersSize != 0) {
                        return userResponse.getData();
                    }
                    return Collections.emptyList();
                });
    }
}
