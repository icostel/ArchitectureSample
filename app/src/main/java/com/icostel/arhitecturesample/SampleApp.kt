package com.icostel.arhitecturesample

import androidx.work.Configuration
import androidx.work.WorkManager
import com.icostel.arhitecturesample.di.AppInjector
import com.icostel.arhitecturesample.di.components.DaggerAppComponent
import com.icostel.arhitecturesample.di.factory.WorkManagerRxFactory

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class SampleApp : DaggerApplication() {

    @Inject
    lateinit var workManagerRxFactory: WorkManagerRxFactory

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppInjector.init(this)

        WorkManager.initialize(
                this@SampleApp,
                Configuration.Builder().run {
                    setWorkerFactory(workManagerRxFactory)
                    build()
                }
        )
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}
