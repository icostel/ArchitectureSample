package com.icostel.arhitecturesample.di.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class HttpClientModule {

    @Singleton
    @Provides
    OkHttpClient providesOkHttpClient() {
        return new OkHttpClient();
    }
}
