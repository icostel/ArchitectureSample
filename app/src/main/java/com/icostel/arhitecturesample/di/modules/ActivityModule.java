package com.icostel.arhitecturesample.di.modules;

import com.icostel.arhitecturesample.ui.loginuser.LoginUserActivity;
import com.icostel.arhitecturesample.ui.main.MainActivity;
import com.icostel.arhitecturesample.ui.newuser.NewUserActivity;
import com.icostel.arhitecturesample.ui.splashscreen.SplashScreenActivity;
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    // Add @ContributesAndroidInjector for each Activity we inject

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract LoginUserActivity contributeLoginUserActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract NewUserActivity contributeNewUserActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract SplashScreenActivity contributeSplashScreenActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract UserDetailsActivity contributeUserDetailsActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract MainActivity contributeMainActivity();
}
