package com.icostel.arhitecturesample.di.modules

import com.icostel.arhitecturesample.repository.IRepository
import com.icostel.arhitecturesample.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepoModule {

    @Binds
    abstract fun bindRepoModule(userRepository: UserRepository): IRepository
}