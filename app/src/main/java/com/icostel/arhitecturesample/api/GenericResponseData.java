package com.icostel.arhitecturesample.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;


// hides the implementation of the responds
public abstract class GenericResponseData<T> {

    static final int USERS = 1;

    static String getResource(int responseType) {
        switch (responseType) {
            case USERS:
            default:
                return "users";
        }
    }

    @SerializedName("items")
    private List<T> items;

    public List<T> getData() {
        return items;
    }

    public void setData(List<T> data) {
        this.items = data;
    }
}
