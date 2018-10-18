package com.icostel.arhitecturesample.di.modules;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.annotation.GlideModule;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
        ViewModelModule.class,
        ApiModule.class,
        HttpClientModule.class,
        DbModule.class}
)
public abstract class AppModule {

    @Binds
    abstract Context bindContext(Application application);
}
