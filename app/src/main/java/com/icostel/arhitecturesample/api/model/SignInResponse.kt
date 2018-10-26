package com.icostel.arhitecturesample.api.model

import com.google.gson.annotations.SerializedName

data class SignInResponse(
        @SerializedName("success")
        val success: Boolean = false,

        @SerializedName("message")
        val message: String = empty,

        @SerializedName("token")
        val token: String = empty) {

    companion object {
        val empty = "empty"
    }

    override fun toString(): String {
        return "SignInResponse(success=$success, message='$message', token='$token')"
    }
}