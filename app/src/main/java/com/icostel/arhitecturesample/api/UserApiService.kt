package com.icostel.arhitecturesample.api


import com.icostel.arhitecturesample.api.model.SignInResponse
import com.icostel.arhitecturesample.api.model.User

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {

    // signs in a user
    @GET("login/")
    fun signInUser(@Header("token") userEmail: String, @Header("token") userPass: String): Call<SignInResponse>

    // returns all users
    @GET("users/")
    fun getUsers(@Header("token") authorization: String): Observable<UserResponseData>

    // returns all users
    @POST("newuser/")
    fun addUser(@Header("token") authorization: String, @Body user: User): Observable<AddUserResponse>

    // gets a user by id
    @GET("users/{userId}/")
    fun getUser(@Header("token") authorization: String, @Path("userId") userId: String): Observable<UserResponseData>
}
