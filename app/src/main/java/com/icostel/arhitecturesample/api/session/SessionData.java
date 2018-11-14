package com.icostel.arhitecturesample.api.session;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class SessionData {
    // token received after user logins with user & pass, used for making API calls
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
