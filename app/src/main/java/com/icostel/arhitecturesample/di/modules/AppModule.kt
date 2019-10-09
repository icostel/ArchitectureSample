package com.icostel.arhitecturesample.di.modules

import android.content.Context
import com.icostel.arhitecturesample.SampleApp
import com.icostel.arhitecturesample.di.factory.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module(includes = [
    ViewModelFactory::class,
    RxWorkerModule::class,
    ViewModelModule::class,
    ApiModule::class,
    PreferenceModule::class,
    HttpClientModule::class,
    DbModule::class,
    UseCaseModule::class,
    ManagerModule::class,
    RepoModule::class,
    UtilsModule::class
])
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: SampleApp): Context
}
