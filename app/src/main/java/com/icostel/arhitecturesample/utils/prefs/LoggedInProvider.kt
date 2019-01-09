package com.icostel.arhitecturesample.utils.prefs

import android.content.SharedPreferences
import com.icostel.arhitecturesample.di.qualifers.PerApp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggedInProvider @Inject constructor(@PerApp private val sharedPreferences: SharedPreferences) : PersistentSetting<Boolean>(sharedPreferences) {

    override fun clazz(): Class<Boolean> {
        return Boolean::class.java
    }

    override fun key(): String {
        return PREF_KEY
    }

    companion object {
        const val PREF_KEY = "user_signed_in"
    }
}
