package com.icostel.arhitecturesample.di.components;

import android.app.Application;

import com.icostel.arhitecturesample.SampleApp;
import com.icostel.arhitecturesample.di.modules.ActivityModule;
import com.icostel.arhitecturesample.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                AppModule.class,
                ActivityModule.class
        }
)

public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(SampleApp sampleApp);
}
