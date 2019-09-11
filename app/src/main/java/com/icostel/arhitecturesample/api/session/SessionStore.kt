package com.icostel.arhitecturesample.api.session

import android.content.SharedPreferences

import com.icostel.arhitecturesample.di.qualifers.PerUser
import com.icostel.commons.utils.prefs.PersistentSetting

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStore
@Inject constructor(@PerUser preferences: SharedPreferences,
                    private val sessionData: SessionData)
    : PersistentSetting<SessionData>(preferences) {

    var userSessionToken: String?
        get() = sessionData.userToken
        set(userSessionToken) {
            sessionData.userToken = userSessionToken
            updateValue(this.sessionData)
        }

    override fun key(): String {
        return "session"
    }

    override fun clazz(): Class<SessionData> {
        return SessionData::class.java
    }
}
