package com.icostel.arhitecturesample.api.session;

import android.content.SharedPreferences;

import com.icostel.arhitecturesample.di.qualifers.PerUser;
import com.icostel.commons.utils.prefs.PersistentSetting;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.Nullable;

@Singleton
public class SessionStore extends PersistentSetting<SessionData> {

    private final SessionData sessionData;

    @Inject
    public SessionStore(@PerUser SharedPreferences preferences, SessionData sessionData) {
        super(preferences);
        this.sessionData = sessionData;
    }

    public void setUserSessionToken(String userSessionToken) {
        this.sessionData.setUserToken(userSessionToken);
        updateValue(this.sessionData);
    }

    public @Nullable
    String getUserSessionToken() {
        return this.sessionData.getUserToken();
    }

    @Override
    protected String key() {
        return "session";
    }

    @Override
    protected Class<SessionData> clazz() {
        return SessionData.class;
    }
}
