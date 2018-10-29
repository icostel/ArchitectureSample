package com.icostel.arhitecturesample.db;


import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.model.User;

import java.util.List;
import java.util.Optional;

import javax.inject.Singleton;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
@Singleton
public abstract class UserDao implements BaseDao<User> {

    // gets all the users if any
    @Query("SELECT * FROM " + Config.Db.USER_TABLE)
    public abstract List<User> getUsers();

    // gets the use with the specific id
    @Query("SELECT * FROM " + Config.Db.USER_TABLE + " WHERE id = :userId")
    public abstract User getUserById(String userId);

    @Query("DELETE FROM " + Config.Db.USER_TABLE)
    public abstract void deleteAllUsers();

    @Query("DELETE FROM " + Config.Db.USER_TABLE + " WHERE id = :userId")
    public abstract void deleteUser(String userId);
}
