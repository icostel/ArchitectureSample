package com.icostel.arhitecturesample.db;


import com.icostel.arhitecturesample.api.model.User;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {User.class},
        version = 1,
        exportSchema = false)
public abstract class AppDb extends RoomDatabase {
    public abstract UserDao getUserDao();
}
