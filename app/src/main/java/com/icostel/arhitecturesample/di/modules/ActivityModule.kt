package com.icostel.arhitecturesample.di.modules

import com.icostel.arhitecturesample.ui.loginuser.LoginUserActivity
import com.icostel.arhitecturesample.ui.main.MainActivity
import com.icostel.arhitecturesample.ui.newuser.NewUserActivity
import com.icostel.arhitecturesample.ui.splashscreen.SplashScreenActivity
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    // Add @ContributesAndroidInjector for each Activity we inject

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeLoginUserActivity(): LoginUserActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeNewUserActivity(): NewUserActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeSplashScreenActivity(): SplashScreenActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeUserDetailsActivity(): UserDetailsActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}
