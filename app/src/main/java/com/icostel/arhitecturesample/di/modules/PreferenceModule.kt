package com.icostel.arhitecturesample.di.modules

import android.content.Context
import android.content.SharedPreferences

import com.icostel.arhitecturesample.Config
import com.icostel.arhitecturesample.di.qualifers.PerApp
import com.icostel.arhitecturesample.di.qualifers.PerUser

import dagger.Module
import dagger.Provides
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

@Module
class PreferenceModule {

    @Provides
    @PerUser
    fun provideUserPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            Config.Pref.PREF_USER,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @PerApp
    fun provideAppPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            Config.Pref.PREF_APP,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
