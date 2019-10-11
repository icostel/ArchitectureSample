package com.icostel.arhitecturesample.di.modules

import com.icostel.arhitecturesample.ui.screens.about.AccountFragment
import com.icostel.arhitecturesample.ui.screens.listusers.ListUsersFragment
import com.icostel.arhitecturesample.ui.screens.userdetails.UserDetailsFragment
import com.icostel.arhitecturesample.utils.error.ErrorFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributesErrorFragment(): ErrorFragment

    @ContributesAndroidInjector
    abstract fun contributesListUsersFragment(): ListUsersFragment

    @ContributesAndroidInjector
    abstract fun contributesUserDetailsFragment(): UserDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributesAccountFragment(): AccountFragment
}
