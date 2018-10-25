package com.icostel.arhitecturesample.api.utils;

import com.google.gson.annotations.SerializedName;

public class SignInResponse {
    @SerializedName("success")
    Boolean success;
    @SerializedName("message")
    String message;
    @SerializedName("token")
    String token;

    public SignInResponse() {
    }

    public SignInResponse(Boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "SignInResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
