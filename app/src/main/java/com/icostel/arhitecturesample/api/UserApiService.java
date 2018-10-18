package com.icostel.arhitecturesample.api;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UserApiService {

    // returns all users
    @GET("users/")
    Observable<UserResponseData> getUsers(@Header("token") String authorization);

    // gets a user by id
    @GET("users/{userId}/")
    Observable<UserResponseData> getUser(@Header("token") String authorization, @Path("userId") String userId);
}
