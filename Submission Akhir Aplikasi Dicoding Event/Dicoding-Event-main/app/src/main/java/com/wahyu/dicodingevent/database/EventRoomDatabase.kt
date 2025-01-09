package com.wahyu.dicodingevent.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wahyu.dicodingevent.database.dao.EventDao
import com.wahyu.dicodingevent.database.dao.FavoriteEventDao
import com.wahyu.dicodingevent.database.entity.EventEntity
import com.wahyu.dicodingevent.database.entity.FavoriteEventEntity

@Database(entities = [EventEntity::class, FavoriteEventEntity::class], version = 1, exportSchema = false)
abstract class EventRoomDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun favoriteEventDao(): FavoriteEventDao

    companion object {
        @Volatile
        private var instance: EventRoomDatabase? = null
        fun getInstance(context: Context): EventRoomDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventRoomDatabase::class.java, "event.db"
                ).build()
            }
    }
}