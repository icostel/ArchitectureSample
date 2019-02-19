package com.icostel.arhitecturesample.di.modules


import com.icostel.arhitecturesample.BuildConfig

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
class HttpClientModule {

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }

        return httpClientBuilder.build()
    }
}
