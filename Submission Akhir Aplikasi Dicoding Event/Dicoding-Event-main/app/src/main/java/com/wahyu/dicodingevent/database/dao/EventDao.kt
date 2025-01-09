package com.wahyu.dicodingevent.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wahyu.dicodingevent.database.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(event: List<EventEntity>)

    @Query ("DELETE FROM data_event")
    fun delete()

    @Query("SELECT * FROM data_event WHERE isActive = 1")
    fun getListActiveEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM data_event WHERE isActive = 0")
    fun getListCompletedEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM data_event WHERE id = :eventId LIMIT 1")
    fun getDetailEvent(eventId: Int): LiveData<EventEntity>

    @Query("SELECT * FROM data_event WHERE id = :id")
    fun getEventId(id: Int): EventEntity?
}