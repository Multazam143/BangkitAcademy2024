package com.dicoding.tugasdicoding.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteEvent: FavoriteEvent)

    @Update
    fun update(favoriteEvent: FavoriteEvent)

    @Delete
    fun delete(favoriteEvent: FavoriteEvent)

    @Query("DELETE FROM FavoriteEvent WHERE id = :eventId")
    fun deleteById(eventId: String)

    @Query("SELECT * from FavoriteEvent ORDER BY id ASC")
    fun getAllFavorites(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM FavoriteEvent WHERE id = :eventId LIMIT 1")
    fun getFavoriteById(eventId: String): LiveData<FavoriteEvent?>
}
