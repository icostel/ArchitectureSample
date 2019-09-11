package com.icostel.arhitecturesample.di.modules

import com.icostel.arhitecturesample.domain.usecases.BaseUseCase
import com.icostel.arhitecturesample.domain.usecases.UserUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {

    @Binds
    abstract fun bindUserUserCaseModule(userUseCase: UserUseCase): BaseUseCase
}