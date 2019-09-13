package com.icostel.arhitecturesample.api.session

import android.content.SharedPreferences

import com.icostel.arhitecturesample.di.qualifers.PerUser
import com.icostel.commons.utils.prefs.PersistentSetting

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStore
@Inject constructor(@PerUser preferences: SharedPreferences)
    : PersistentSetting<SessionData>(preferences) {

    private var userSessionToken: String? = null

    init {
        userSessionToken = instantValue()?.userToken
    }

    fun getUserToken(): String? {
        return instantValue()?.userToken
    }

    fun setUserToken(userToken: String?) {
        updateValue(SessionData(userToken))
    }

    override fun key(): String {
        return "session"
    }

    override fun clazz(): Class<SessionData> {
        return SessionData::class.java
    }
}
