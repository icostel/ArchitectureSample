package com.icostel.arhitecturesample.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.di.qualifers.PerUser;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferenceModule {
    @Provides
    @PerUser
    SharedPreferences provideUserPreferences(Context context) {
        return context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
    }
}
