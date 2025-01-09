package com.wahyu.dicodingevent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wahyu.dicodingevent.database.entity.FavoriteEventEntity

@Dao
interface FavoriteEventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteEvent: FavoriteEventEntity)

    @Query("DELETE FROM list_favorite_event WHERE id = :id")
    fun deleteFavoriteEvent(id: Int)

    @Query("SELECT * FROM list_favorite_event")
    fun getFavoriteEvent(): LiveData<List<FavoriteEventEntity>>

    @Query("SELECT * FROM list_favorite_event WHERE id = :id")
    fun getFavoriteEventById(id: Int): LiveData<FavoriteEventEntity?>
}