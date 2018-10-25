package com.icostel.arhitecturesample.api.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class SessionData {
    //TODO maybe multiple providers with sign in status and token
    String userToken = null;

    @Inject
    public SessionData() {

    }

    String getUserToken() {
        return userToken;
    }

    void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
