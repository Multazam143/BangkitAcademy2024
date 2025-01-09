package com.dicoding.tugasdicoding.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoriteEvent::class], version = 2, exportSchema = false)
abstract class FavoriteEventRoomDatabase : RoomDatabase() {
    abstract fun favoriteEventDao(): FavoriteEventDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteEventRoomDatabase? = null

        fun getDatabase(context: Context): FavoriteEventRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteEventRoomDatabase::class.java,
                    "favorite_event_database"
                )
                    .addMigrations(MIGRATION_1_2)   // Add migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Define the migration for version 1 to 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {  // Changed 'database' to 'db'
        // Example: Add a new column 'new_column' in 'favorite_event' table
        db.execSQL("ALTER TABLE favorite_event ADD COLUMN new_column_name TEXT")
    }
}
