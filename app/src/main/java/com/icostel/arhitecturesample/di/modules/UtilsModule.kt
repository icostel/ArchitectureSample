package com.icostel.arhitecturesample.di.modules

import android.content.Context
import android.net.ConnectivityManager
import com.icostel.arhitecturesample.SampleApp
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {

    @Provides
    fun providesConnectionManager(context: SampleApp): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}
