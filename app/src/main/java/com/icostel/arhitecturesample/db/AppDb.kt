package com.icostel.arhitecturesample.db


import com.icostel.arhitecturesample.api.model.User

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract val userDao: UserDao
}
