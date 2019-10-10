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

    fun getUserToken(): String? {
        return getValue()?.userToken
    }

    fun getKeepLogin(): Boolean? {
        return getValue()?.keepLogIn
    }

    fun setKeepLogin(keep: Boolean) {
        if (getValue() == null) {
            updateValue(SessionData(keepLogIn = keep))
        } else {
            updateValue(getValue()!!.copy(keepLogIn = keep))
        }
    }

    fun setUserToken(userToken: String?) {
        if (getValue() == null) {
            updateValue(SessionData(userToken = userToken))
        } else {
            updateValue(getValue()!!.copy(userToken = userToken))
        }
    }

    //TODO why this is not ecrypted ?????
    override fun key(): String {
        return "session"
    }

    override fun clazz(): Class<SessionData> {
        return SessionData::class.java
    }
}
