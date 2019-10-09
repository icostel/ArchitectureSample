package com.icostel.arhitecturesample.di.modules

import com.icostel.arhitecturesample.di.factory.BaseWorkerFactory
import com.icostel.arhitecturesample.di.keys.WorkerKey
import com.icostel.arhitecturesample.work.SyncWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface RxWorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(SyncWorker::class)
    fun bindSyncWorkerFactory(factory: SyncWorker.Factory): BaseWorkerFactory
}