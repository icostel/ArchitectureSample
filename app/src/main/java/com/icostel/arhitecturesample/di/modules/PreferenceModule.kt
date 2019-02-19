package com.icostel.arhitecturesample.di.modules

import android.content.Context
import android.content.SharedPreferences

import com.icostel.arhitecturesample.Config
import com.icostel.arhitecturesample.di.qualifers.PerApp
import com.icostel.arhitecturesample.di.qualifers.PerUser

import dagger.Module
import dagger.Provides

@Module
class PreferenceModule {
    @Provides
    @PerUser
    fun provideUserPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Config.Pref.PREF_USER, Context.MODE_PRIVATE)
    }

    @Provides
    @PerApp
    fun provideAppPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Config.Pref.PREF_APP, Context.MODE_PRIVATE)
    }
}
