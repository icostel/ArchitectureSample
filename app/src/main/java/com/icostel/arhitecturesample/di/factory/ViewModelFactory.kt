package com.icostel.arhitecturesample.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Module
class ViewModelFactory {

    @Provides
    @Singleton
    fun provideViewModelFactory(
            providers: Map<Class<out ViewModel>,
            @JvmSuppressWildcards Provider<ViewModel>>) = object : ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return requireNotNull(providers[modelClass]).get() as T
        }
    }
}
