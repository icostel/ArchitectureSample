package com.icostel.arhitecturesample.di.factory

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider


//TODO @Module this and refactor injection to base type
class WorkManagerRxFactory @Inject constructor(
        // maps workers to their factories using the bindings from WorkerModule
        private val workerFactories: Map<Class<out RxWorker>, @JvmSuppressWildcards Provider<BaseWorkerFactory>>

): WorkerFactory() {

    override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters): RxWorker? {

        val foundEntry = workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factoryProvider = foundEntry?.value
                ?: throw IllegalArgumentException("unknown worker class name: $workerClassName")
        return factoryProvider.get().createWorker(appContext, workerParameters)
    }
}