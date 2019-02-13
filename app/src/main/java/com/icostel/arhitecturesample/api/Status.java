package com.icostel.arhitecturesample.api;

public class Status {
    public enum Type {NOT_STARTED, IN_PROGRESS, SUCCESS, ERROR, LOADING, CALL_ERROR, INPUTS_ERROR}

    private Type type;

    private Status(Type type) {
        this.type = type;
    }

    public static com.icostel.arhitecturesample.api.Status NotStarted() {
        return new Status(Type.NOT_STARTED);
    }

    public static com.icostel.arhitecturesample.api.Status InProgress() {
        return new Status(Type.IN_PROGRESS);
    }

    public static com.icostel.arhitecturesample.api.Status Success() {
        return new Status(Type.SUCCESS);
    }

    public static com.icostel.arhitecturesample.api.Status Error() {
        return new Status(Type.CALL_ERROR);
    }

    public static com.icostel.arhitecturesample.api.Status Loading() {
        return new Status(Type.LOADING);
    }

    public Type getType() {
        return this.type;
    }
}
