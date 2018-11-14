package com.icostel.arhitecturesample.api;

public class SignInStatus {
    public enum Status {NOT_STARTED, IN_PROGRESS, SUCCESS, ERROR, LOADING, CALL_ERROR, INPUTS_ERROR}

    Status status;

    public SignInStatus(Status status) {
        this.status = status;
    }

    public static SignInStatus NotStarted() {
        return new SignInStatus(Status.NOT_STARTED);
    }

    public static SignInStatus InProgress() {
        return new SignInStatus(Status.IN_PROGRESS);
    }

    public static SignInStatus Success() {
        return new SignInStatus(Status.SUCCESS);
    }

    public static SignInStatus Error() {
        return new SignInStatus(Status.CALL_ERROR);
    }

    public static SignInStatus Loading() {
        return new SignInStatus(Status.LOADING);
    }

    public Status getStatus() {
        return this.status;
    }
}
