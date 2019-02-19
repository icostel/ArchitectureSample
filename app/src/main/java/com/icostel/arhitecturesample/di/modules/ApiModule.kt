package com.icostel.arhitecturesample.di.modules

import com.icostel.arhitecturesample.Config
import com.icostel.arhitecturesample.api.UserApiService
import com.icostel.arhitecturesample.api.MockServer
import com.icostel.arhitecturesample.api.utils.LiveDataCallAdapterFactory

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideApiService(httpClient: OkHttpClient, mockServer: MockServer): UserApiService {

        return Retrofit.Builder()
                .baseUrl(Config.REST_ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mockServer.url)
                .client(httpClient)
                .build()
                .create(UserApiService::class.java)
    }
}
