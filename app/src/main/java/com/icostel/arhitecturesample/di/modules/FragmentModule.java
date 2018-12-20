package com.icostel.arhitecturesample.di.modules;

import com.icostel.arhitecturesample.utils.error.ErrorFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract ErrorFragment contributesErrorFragment();
}
