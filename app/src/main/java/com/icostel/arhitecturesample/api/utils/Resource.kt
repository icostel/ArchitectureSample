package com.icostel.arhitecturesample.api.utils

import com.icostel.arhitecturesample.api.SignInStatus

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: SignInStatus.Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SignInStatus.Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(SignInStatus.Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(SignInStatus.Status.LOADING, data, null)
        }
    }
}