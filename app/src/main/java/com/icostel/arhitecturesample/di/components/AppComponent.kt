package com.icostel.arhitecturesample.di.components

import com.icostel.arhitecturesample.SampleApp
import com.icostel.arhitecturesample.di.modules.ActivityModule
import com.icostel.arhitecturesample.di.modules.AppModule

import javax.inject.Singleton

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityModule::class
])
interface AppComponent: AndroidInjector<SampleApp> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<SampleApp>
}
