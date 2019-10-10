package com.icostel.arhitecturesample.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.icostel.arhitecturesample.di.qualifers.PerApp
import com.icostel.arhitecturesample.di.qualifers.PerUser
import com.icostel.commons.utils.EncryptedSharedPrefs
import dagger.Module
import dagger.Provides

@Module
class PreferenceModule {

    @Provides
    @PerUser
    fun provideUserPreferences(context: Context): SharedPreferences {
        return EncryptedSharedPrefs.build(context, EncryptedSharedPrefs.Pref.PREF_USER)
    }

    @Provides
    @PerApp
    fun provideAppPreferences(context: Context): SharedPreferences {
        return EncryptedSharedPrefs.build(context, EncryptedSharedPrefs.Pref.PREF_APP)
    }
}
