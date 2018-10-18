package com.icostel.arhitecturesample.di.modules;

import com.icostel.arhitecturesample.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    // Add @ContributesAndroidInjector for each Activity we inject
    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract MainActivity contributeMainActivity();
}
