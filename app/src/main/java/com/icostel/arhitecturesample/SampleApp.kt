package com.icostel.arhitecturesample

import com.icostel.arhitecturesample.di.AppInjector
import com.icostel.arhitecturesample.di.components.DaggerAppComponent

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class SampleApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppInjector.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}
