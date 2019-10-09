package com.icostel.arhitecturesample.di.modules

import androidx.room.Room
import com.icostel.arhitecturesample.Config
import com.icostel.arhitecturesample.SampleApp
import com.icostel.arhitecturesample.db.AppDb
import com.icostel.arhitecturesample.db.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

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
