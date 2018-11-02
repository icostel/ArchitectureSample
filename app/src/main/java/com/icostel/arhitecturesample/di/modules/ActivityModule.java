package com.icostel.arhitecturesample.di.modules;

import com.icostel.arhitecturesample.ui.listusers.UserListActivity;
import com.icostel.arhitecturesample.ui.loginuser.LoginUserActivity;
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    // Add @ContributesAndroidInjector for each Activity we inject
    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract UserListActivity contributeUserListActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract UserDetailsActivity contributeUserDetailsActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract LoginUserActivity contributeLoginUserActivity();
}
