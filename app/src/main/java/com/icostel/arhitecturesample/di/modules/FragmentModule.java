package com.icostel.arhitecturesample.di.modules;

import com.icostel.arhitecturesample.ui.about.AboutFragment;
import com.icostel.arhitecturesample.ui.listusers.ListUsersFragment;
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsFragment;
import com.icostel.arhitecturesample.utils.error.ErrorFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract ErrorFragment contributesErrorFragment();

    @ContributesAndroidInjector
    abstract ListUsersFragment contributesListUsersFragment();

    @ContributesAndroidInjector
    abstract UserDetailsFragment contributesUserDetailsFragment();

    @ContributesAndroidInjector
    abstract AboutFragment contributesAboutFragment();
}
