package com.icostel.arhitecturesample.di.modules;

import android.app.Application;

import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.db.AppDb;
import com.icostel.arhitecturesample.db.UserDao;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {
    @Singleton
    @Provides
    AppDb provideDb(Application app) {
        return Room
                .databaseBuilder(app, AppDb.class, Config.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    UserDao providesUserDao(AppDb db) {
        return db.getUserDao();
    }
}
