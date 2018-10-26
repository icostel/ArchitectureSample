package com.icostel.arhitecturesample.api.utils;

public class SignInStatus {
    public enum Status {NotStarted, InProgress, Success, CallError, InputsError}

    Status status;

    public SignInStatus(Status status) {
        this.status = status;
    }

    public static SignInStatus NotStarted() {
        return new SignInStatus(Status.NotStarted);
    }

    public static SignInStatus InProgress() {
        return new SignInStatus(Status.InProgress);
    }

    public static SignInStatus Success() {
        return new SignInStatus(Status.Success);
    }

    public static SignInStatus Error() {
        return new SignInStatus(Status.CallError);
    }

    public Status getStatus() {
        return this.status;
    }
}
