package com.icostel.arhitecturesample.api.model

import com.google.gson.annotations.SerializedName

data class SignInResponse(
        @SerializedName("success")
        val success: Boolean = false,

        @SerializedName("message")
        val message: String = "",

        @SerializedName("token")
        val token: String = "")