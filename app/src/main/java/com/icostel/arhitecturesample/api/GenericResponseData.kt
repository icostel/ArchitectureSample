package com.icostel.arhitecturesample.api

import com.google.gson.annotations.SerializedName


// hides the implementation of the response
abstract class GenericResponseData<T> {

    @SerializedName("items")
    var data: List<T>? = null

    companion object {
        const val USERS = 1
        fun getResource(responseType: Int): String {
            return when (responseType) {
                USERS -> "users"
                else -> "users"
            }
        }
    }
}
