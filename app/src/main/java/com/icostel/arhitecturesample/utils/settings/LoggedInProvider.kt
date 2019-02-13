package com.icostel.arhitecturesample.utils.settings

import android.content.SharedPreferences
import com.icostel.arhitecturesample.api.session.SessionData
import com.icostel.arhitecturesample.api.session.SessionStore
import com.icostel.arhitecturesample.di.qualifers.PerApp
import com.icostel.commons.utils.prefs.PersistentSetting
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggedInProvider @Inject constructor(@PerApp private val sharedPreferences: SharedPreferences) : PersistentSetting<Boolean>(sharedPreferences) {

    @Inject
    lateinit var sessionStore: SessionStore

    override fun clazz(): Class<Boolean> {
        return Boolean::class.java
    }

    override fun key(): String {
        return PREF_KEY
    }

    override fun updateValue(value: Boolean?) {
        super.updateValue(value)

        value?.let {
            if (value == false) {
                sessionStore.userSessionToken = ""
            }
        }
    }

    companion object {
        const val PREF_KEY = "user_signed_in"
    }
}
