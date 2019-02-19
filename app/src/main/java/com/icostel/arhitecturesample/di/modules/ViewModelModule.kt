package com.icostel.arhitecturesample.di.modules


import com.icostel.arhitecturesample.di.ViewModelKey
import com.icostel.arhitecturesample.ui.loginuser.LoginUserViewModel
import com.icostel.arhitecturesample.ui.listusers.ListUsersViewModel
import com.icostel.arhitecturesample.ui.newuser.NewUserViewModel
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsViewModel
import com.icostel.arhitecturesample.utils.error.ErrorViewModel

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ListUsersViewModel::class)
    abstract fun bindListUsersViewModel(listUsersViewModel: ListUsersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ErrorViewModel::class)
    abstract fun bindErrorViewModel(listUsersViewModel: ErrorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginUserViewModel::class)
    abstract fun bindLoginUserViewModel(loginUserViewModel: LoginUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailsViewModel::class)
    abstract fun bindUserDetailsViewModel(userDetailsViewModel: UserDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewUserViewModel::class)
    abstract fun bindNewUserViewModel(newUserViewModel: NewUserViewModel): ViewModel
}
