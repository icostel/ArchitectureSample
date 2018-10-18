package com.icostel.arhitecturesample.di.modules;


import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.di.ViewModelKey;
import com.icostel.arhitecturesample.ui.main.MainActivityViewModel;
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    // Add for each view model
    //
    // @Binds
    // @IntoMap
    // @ViewModelKey(ViewModel.class)
    // abstract ViewModel bind[ViewModelName]([ViewModelName] viewModelVar);

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel mainActivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailsViewModel.class)
    abstract ViewModel bindUserDetailsViewModel(UserDetailsViewModel userDetailsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
