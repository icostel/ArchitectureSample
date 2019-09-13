package com.icostel.arhitecturesample.db


import com.icostel.arhitecturesample.Config
import com.icostel.arhitecturesample.api.model.User

import javax.inject.Singleton

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
@Singleton
abstract class UserDao : BaseDao<User> {

    // gets all the users if any
    @Query("SELECT * FROM " + Config.Db.USER_TABLE)
    abstract fun getUsers(): List<User>

    // get all user from db searching by first name
    @Query("SELECT * FROM " + Config.Db.USER_TABLE + " WHERE first_name LIKE LOWER(:name)")
    abstract fun getUsers(name: String): List<User>

    @Query("SELECT COUNT(id) FROM " + Config.Db.USER_TABLE)
    abstract fun getUserCount(): Int

    // gets the user with the specific id
    @Query("SELECT * FROM " + Config.Db.USER_TABLE + " WHERE id = :userId")
    abstract fun getUserById(userId: String): Single<User>

    @Query("DELETE FROM " + Config.Db.USER_TABLE)
    abstract fun deleteAllUsers()

    @Query("DELETE FROM " + Config.Db.USER_TABLE + " WHERE id = :userId")
    abstract fun deleteUser(userId: String)
}
