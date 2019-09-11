package com.icostel.arhitecturesample.db

import android.database.sqlite.SQLiteConstraintException

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update


/**
 * A generic dao with basic operations on a type <T> item
</T> */
interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param item the item to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(item: T)

    /**
     * Update an object from the database.
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(item: T)

    /**
     * Delete an object from the database
     */
    @Delete
    fun delete(item: T)

    /**
     * Tries and updates first and insert second the item
     * If the update fails the item will be inserted
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun upsert(item: T) {
        try {
            update(item)
        } catch (exception: SQLiteConstraintException) {
            insert(item)
        }

    }
}
