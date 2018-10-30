package com.icostel.arhitecturesample.db;

import android.database.sqlite.SQLiteConstraintException;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;


/**
 * A generic dao with basic operations on a type <T> item
 */
public interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param item the item to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(T item);

    /**
     * Update an object from the database.
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(T item);

    /**
     * Delete an object from the database
     */
    @Delete
    void delete(T item);

    /**
     * Tries and updates first and insert second the list of items
     * If the update fails the item will be inserted
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    default void upsert(List<T> items) {
        for (T item : items) {
            try {
                insert(item);
            } catch (SQLiteConstraintException exception) {
                update(item);
            }
        }
    }

    /**
     * Tries and updates first and insert second the item
     * If the update fails the item will be inserted
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    default void upsert(T item) {
        try {
            insert(item);
        } catch (SQLiteConstraintException exception) {
            update(item);
        }
    }
}
