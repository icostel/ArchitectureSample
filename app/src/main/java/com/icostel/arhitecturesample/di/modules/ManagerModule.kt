package com.icostel.arhitecturesample.di.modules

import com.icostel.arhitecturesample.manager.SnackBarManager
import com.icostel.arhitecturesample.manager.SnackBarManagerImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ManagerModule {

    @Binds
    abstract fun bindSnackBarManager(snackBarManagerImpl: SnackBarManagerImpl): SnackBarManager

}