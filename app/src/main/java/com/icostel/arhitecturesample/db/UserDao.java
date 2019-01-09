package com.icostel.arhitecturesample.db;


import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.api.model.User;

import java.util.List;

import javax.inject.Singleton;

import androidx.room.Dao;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
@Singleton
public abstract class UserDao implements BaseDao<User> {

    // gets all the users if any
    @Query("SELECT * FROM " + Config.Db.USER_TABLE)
    public abstract Single<List<User>> getUsers();

    // get all user from db searching by first name
    @Query("SELECT * FROM " + Config.Db.USER_TABLE + " WHERE first_name LIKE LOWER(:name)")
    public abstract Single<List<User>> getUsers(String name);

    // gets the user with the specific id
    @Query("SELECT * FROM " + Config.Db.USER_TABLE + " WHERE id = :userId")
    public abstract Single<User> getUserById(String userId);

    @Query("DELETE FROM " + Config.Db.USER_TABLE)
    public abstract void deleteAllUsers();

    @Query("DELETE FROM " + Config.Db.USER_TABLE + " WHERE id = :userId")
    public abstract void deleteUser(String userId);
}
