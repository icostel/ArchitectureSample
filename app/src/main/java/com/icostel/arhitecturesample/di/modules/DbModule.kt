package com.icostel.arhitecturesample.di.modules

import com.icostel.arhitecturesample.Config
import com.icostel.arhitecturesample.db.AppDb
import com.icostel.arhitecturesample.db.UserDao

import javax.inject.Singleton

import androidx.room.Room
import com.icostel.arhitecturesample.SampleApp
import dagger.Module
import dagger.Provides

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDb(app: SampleApp): AppDb {
        return Room
                .databaseBuilder(app, AppDb::class.java, Config.Db.APP_DB)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun providesUserDao(db: AppDb): UserDao {
        return db.userDao
    }
}
