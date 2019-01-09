package com.icostel.arhitecturesample.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.di.qualifers.PerApp;
import com.icostel.arhitecturesample.di.qualifers.PerUser;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferenceModule {
    @Provides
    @PerUser
    SharedPreferences provideUserPreferences(Context context) {
        return context.getSharedPreferences(Config.Pref.PREF_USER, Context.MODE_PRIVATE);
    }

    @Provides
    @PerApp
    SharedPreferences provideAppPreferences(Context context) {
        return context.getSharedPreferences(Config.Pref.PREF_APP, Context.MODE_PRIVATE);
    }
}
