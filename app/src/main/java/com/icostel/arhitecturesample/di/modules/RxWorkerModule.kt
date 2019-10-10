package com.icostel.arhitecturesample.di.modules

import com.icostel.arhitecturesample.di.factory.BaseWorkerFactory
import com.icostel.arhitecturesample.di.keys.WorkerKey
import com.icostel.arhitecturesample.repository.UserRepository
import com.icostel.arhitecturesample.work.SyncWorker
import com.icostel.arhitecturesample.work.SyncWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
class RxWorkerModule {

    @Provides
    @IntoMap
    @WorkerKey(SyncWorker::class)
    fun provideSyncWorkerFactory(userRepository: Provider<UserRepository>): BaseWorkerFactory {
        return SyncWorkerFactory(userRepository)
    }
}