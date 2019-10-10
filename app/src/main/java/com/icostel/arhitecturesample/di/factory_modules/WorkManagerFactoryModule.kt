package com.icostel.arhitecturesample.di.factory_modules

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.icostel.arhitecturesample.work.BaseWorkerFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton


@Module
class WorkManagerFactoryModule {

    @Provides
    @Singleton
    fun providesWorkerManagerFactory(
            workerFactories: Map<Class<out RxWorker>, @JvmSuppressWildcards Provider<BaseWorkerFactory>>
    ): WorkerFactory = object : WorkerFactory() {
        override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): RxWorker? {
            val foundEntry = workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
            val factoryProvider = foundEntry?.value
                    ?: throw IllegalArgumentException("unknown worker class name: $workerClassName")
            return factoryProvider.get().createWorker(appContext, workerParameters)
        }

    }
}