package com.icostel.arhitecturesample.di.components

import com.icostel.arhitecturesample.SampleApp
import com.icostel.arhitecturesample.di.modules.ActivityModule
import com.icostel.arhitecturesample.di.modules.AppModule

import javax.inject.Singleton

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityModule::class])
interface AppComponent: AndroidInjector<SampleApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<SampleApp>()
}
