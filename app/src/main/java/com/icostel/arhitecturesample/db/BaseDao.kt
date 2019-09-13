package com.icostel.arhitecturesample.db

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import timber.log.Timber

/**
 * A generic dao with basic operations on a type <T> item
</T> */
interface BaseDao<T> {

    companion object {
        private const val TAG = "BaseDao"
    }

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
    @Update(onConflict = OnConflictStrategy.FAIL)
    fun update(item: T)

    /**
     * Delete an object from the database
     */
    @Delete
    fun delete(item: T)

    /**
     * Tries and inserts first and updates second the item
     */
    @Transaction
    fun upsert(item: T) {
        try {
            insert(item)
        } catch (exception: SQLiteConstraintException) {
            Timber.d("$TAG insert failed, trying update")
            try {
                update(item)
            } catch (exception: SQLiteConstraintException) {
                Timber.d("$TAG update failed, nothing to do...")
            }
        }
    }
}
