package com.icostel.arhitecturesample.di.modules;


import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.di.ViewModelKey;
import com.icostel.arhitecturesample.ui.loginuser.LoginUserViewModel;
import com.icostel.arhitecturesample.ui.listusers.ListUsersViewModel;
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsViewModel;
import com.icostel.arhitecturesample.utils.error.ErrorViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ListUsersViewModel.class)
    abstract ViewModel bindListUsersViewModel(ListUsersViewModel listUsersViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ErrorViewModel.class)
    abstract ViewModel bindErrorViewModel(ErrorViewModel listUsersViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginUserViewModel.class)
    abstract ViewModel bindLoginUserViewModel(LoginUserViewModel loginUserViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailsViewModel.class)
    abstract ViewModel bindUserDetailsViewModel(UserDetailsViewModel userDetailsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
